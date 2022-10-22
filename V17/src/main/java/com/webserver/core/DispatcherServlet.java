package com.webserver.core;

import com.webserver.annoations.Controller;
import com.webserver.annoations.RequestMapping;
import com.webserver.controller.UserController;
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
        // path: /index.html

        //当前请求路径是否对应为一个业务处理
        /*
            当我们得到本次请求路径path的值后，我们首先要查看是否为请求业务:
            1:扫描controller包下的所有类
            2:查看哪些被注解@Controller标注的过的类(只有被该注解标注的类才认可为业务处理类)
            3:遍历这些类，并获取他们的所有方法，并查看哪些时业务方法
              只有被注解@RequestMapping标注的方法才是业务方法
            4:遍历业务方法时比对该方法上@RequestMapping中传递的参数值是否与本次请求
              路径path值一致?如果一致则说明本次请求就应当由该方法进行处理
              因此利用反射机制调用该方法进行处理。
            5:如果扫描了所有的Controller中所有的业务方法，均未找到与本次请求匹配的路径
              则说明本次请求并非处理业务，那么执行下面请求静态资源的操作
         */
        try {
            Method m = HandlerMapping.getMethod(path);
            if (m!=null){
                Class cls = m.getDeclaringClass();
                Object o = cls.newInstance();
                m.invoke(o,request,response);
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
