package com.webserver.core.controller;

import com.webserver.common.Judge;
import com.webserver.core.entity.User;
import com.webserver.http.HttpServletRequest;
import com.webserver.http.HttpServletResponse;

import java.io.*;
import java.util.Arrays;
import java.util.List;

public class UserController {
    private static File userDir;

    static {
        userDir = new File("./userDir");
        if (!userDir.exists()) {
            userDir.mkdirs();
        }
    }

    public void reg(HttpServletRequest request, HttpServletResponse response) throws IOException {
        System.out.println("开始注册用户！！");
        List<String> parameters = Arrays.asList("username", "password", "nickname", "age");
        String username = request.getParameter("username");
        String password = request.getParameter("password");
        String nickname = request.getParameter("nickname");
        String ageStr = request.getParameter("age");
        int age = Integer.parseInt(ageStr);
        System.out.println(username + "," + password + "," + nickname + "," + ageStr);
        if (Judge.isParError(parameters, request) ||
                !ageStr.matches("[0-9]+")) {
            System.out.println("有问题啊哥");
            response.sendRedirect("/reg_info_error.html");
        }
        User user = new User(username, password, nickname, age);
        File file = new File(userDir, username + ".obj");
        //如果用户存在，则返回失败，并设置302重定向
        if (file.exists()){
            //
            response.sendRedirect("/haveUser.html");
            return;
        }
        FileOutputStream fos = new FileOutputStream(file);
        ObjectOutputStream oos = new ObjectOutputStream(fos);
        oos.writeObject(user);
        System.out.println("创建成功" + user);
        response.sendRedirect("/reg_user_success.html");
        oos.close();
    }
}
