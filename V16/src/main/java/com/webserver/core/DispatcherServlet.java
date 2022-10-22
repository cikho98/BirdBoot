package com.webserver.core;

import com.webserver.annoations.Controller;
import com.webserver.annoations.RequestMapping;
import com.webserver.core.controller.UserController;
import com.webserver.http.HttpServletRequest;
import com.webserver.http.HttpServletResponse;

import java.io.File;
import java.io.IOException;
import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.net.URISyntaxException;
import java.util.Arrays;

/**
 * 该类实际上是SpringMVC框架提供的类，用于接手Tomcat处理一次HTTP交互中
 * 处理请求环节的工作，也是Tomcat和SpringMVC框架整合中关键的一个类
 */
public class DispatcherServlet {
    //构造方法私有，单例模式
    private static DispatcherServlet instance = new DispatcherServlet();
    //类的根目录
    private static File root;
    //其他文件目录
    private static File staticDir;
    //controller目录
    private static File controllerDir;
    private static String controllerPath = "com/webserver/core/controller";


    static {
        try {
            root = new File(
                    DispatcherServlet.class.getClassLoader().getResource(".").toURI()
            );
            staticDir = new File(root, "static");
            controllerDir = new File(root, controllerPath);
        } catch (URISyntaxException e) {
            e.printStackTrace();
        }
    }

    private DispatcherServlet() {
    }

    public static DispatcherServlet getInstance() {
        return instance;
    }

    public void service(HttpServletRequest request, HttpServletResponse response) throws IOException, ClassNotFoundException {
        String path = request.getRequestURI();
        File file = new File(staticDir, path);
        try {
            if (file.isFile()) {
                response.setContentFile(file);
            } else{
                boolean havaController = searchController(path,request,response);
                if (havaController) {
                    response.setStatusCode(404);
                    response.setStatusReason("NotFound");
                    file = new File(staticDir, "404.html");
                    response.setContentFile(file);
                }
            }
            response.addHeader("Server", "BirdWebServer");
        } catch (NoSuchMethodException e) {
            e.printStackTrace();
        } catch (InvocationTargetException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        } catch (InstantiationException e) {
            e.printStackTrace();
        }

    }

    public boolean searchController(String path,HttpServletRequest request, HttpServletResponse response) throws ClassNotFoundException, NoSuchMethodException, InvocationTargetException, IllegalAccessException, InstantiationException {
        String packageName = controllerPath.replaceAll("/",".");
        File[] subs = controllerDir.listFiles(f -> f.getName().endsWith(".class"));
        if (subs!=null) {
            for (File file : subs) {
                String fileName = file.getName();
                fileName = fileName.substring(0, fileName.indexOf("."));
                Class cls = Class.forName(packageName + "." + fileName);
                if (cls.isAnnotationPresent(Controller.class)) {
                    Constructor con = cls.getDeclaredConstructor();
                    Object o = con.newInstance();
                    Method[] methods = cls.getMethods();
                    for (Method method : methods) {
                        if (method.isAnnotationPresent(RequestMapping.class)) {
                            RequestMapping rm = method.getAnnotation(RequestMapping.class);
                            if (path.equals(rm.value())) {
                                method.invoke(o,request,response);
                                return false;
                            }
                        }
                    }
                }
            }
        }
        return true;
    }

}
