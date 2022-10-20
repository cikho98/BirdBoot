package com.webserver.core;

import com.webserver.http.HttpServletRequest;
import com.webserver.http.HttpServletResponse;

import java.io.File;
import java.net.URISyntaxException;

public class DispatcherServlet {
    private static DispatcherServlet instance= new DispatcherServlet();
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

    private  DispatcherServlet(){

    }

    public static DispatcherServlet getInstance(){
        return instance;
    }


    public void service(HttpServletRequest request, HttpServletResponse response){
        String path = request.getUri();
        File file = new File(staticDir, path);
        if (file.isFile()) {
            response.setContentFile(file);
        } else {
            response.setStatusCode(404);
            response.setStatusReason("NotFound");
            file = new File(staticDir, "404.html");
            response.setContentFile(file);
        }
        response.addHeader("Server","BirdWebServer");
    }

}
