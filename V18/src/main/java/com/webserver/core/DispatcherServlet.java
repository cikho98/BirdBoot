package com.webserver.core;

import com.webserver.http.HttpServletRequest;
import com.webserver.http.HttpServletResponse;

import java.io.File;
import java.lang.reflect.Method;
import java.net.URISyntaxException;

/**
 * 该类实际上是SpringMVC框架提供的类，用于接手Tomcat处理一次HTTP交互中
 * 处理请求环节的工作，也是Tomcat和SpringMVC框架整合中关键的一个类
 */
public class DispatcherServlet {
    private static DispatcherServlet instance = new DispatcherServlet();
    private static File root;
    private static File staticDir;

    static {
        try {
            root = new File(
                    DispatcherServlet.class.getClassLoader().getResource(".").toURI()
            );
            staticDir = new File(root, "static");
        } catch (URISyntaxException e) {
            e.printStackTrace();
        }
    }

    private DispatcherServlet() {
    }

    public static DispatcherServlet getInstance() {
        return instance;
    }

    public void service(HttpServletRequest request, HttpServletResponse response) {
        /*
            这里不能再使用uri作为请求路径判断了，因为uri可能含有参数
            而参数不是固定不变的内容，无法参与确定值的判断。
         */
        String path = request.getRequestURI();
        System.out.println("抽象路径:" + path);
        //当前请求路径是否对应为一个业务处理
        try {
            Method method = HandlerMapping.getMethod(path);
            if(method!=null){
                //该方法所属的类(对应的Controller的类对象)
                Class cls = method.getDeclaringClass();
                Object o = cls.newInstance();//用类对象实例化该Controller
                method.invoke(o,request,response);//反射调用这个方法
                return;
            }

        } catch (Exception e) {
            //处理业务如果出现了异常应当设置response响应500类错误
        }


        File file = new File(staticDir, path);
        if (file.isFile()) {
            response.setContentFile(file);
        } else {
            response.setStatusCode(404);
            response.setStatusReason("NotFound");
            file = new File(staticDir, "404.html");
            response.setContentFile(file);
        }
        response.addHeader("Server", "BirdWebServer");

    }
}
