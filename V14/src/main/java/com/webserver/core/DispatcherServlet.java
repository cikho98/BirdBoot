package com.webserver.core;

import com.webserver.core.controller.UserController;
import com.webserver.http.HttpServletRequest;
import com.webserver.http.HttpServletResponse;

import java.io.File;
import java.io.IOException;
import java.net.URISyntaxException;

/**
 * 该类实际上是SpringMVC框架提供的类，用于接手Tomcat处理一次HTTP交互中
 * 处理请求环节的工作，也是Tomcat和SpringMVC框架整合中关键的一个类
 */
public class DispatcherServlet {
    private static DispatcherServlet instance = new DispatcherServlet();
    private static File root;
    private static File staticDir;
    static{
        try {
            root = new File(
                    DispatcherServlet.class.getClassLoader().getResource(".").toURI()
            );
            staticDir = new File(root, "static");
        } catch (URISyntaxException e) {
            e.printStackTrace();
        }
    }
    private DispatcherServlet(){}

    public static DispatcherServlet getInstance(){
        return instance;
    }

    public void service(HttpServletRequest request, HttpServletResponse response) throws IOException {
        String path = request.getRequestURI();
        File file = new File(staticDir, path);

        if ("/regUser".equals(path)){
            UserController user = new UserController();
            user.reg(request,response);
        }else {
            if(file.isFile()){
                response.setContentFile(file);
            }else{
                response.setStatusCode(404);
                response.setStatusReason("NotFound");
                file = new File(staticDir,"404.html");
                response.setContentFile(file);
            }
        }
        response.addHeader("Server","BirdWebServer");

    }
}
