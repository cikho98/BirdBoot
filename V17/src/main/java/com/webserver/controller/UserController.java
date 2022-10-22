package com.webserver.controller;

import com.webserver.annoations.Controller;
import com.webserver.annoations.RequestMapping;
import com.webserver.entity.User;
import com.webserver.http.HttpServletRequest;
import com.webserver.http.HttpServletResponse;

import java.io.*;

/**
 * 当前类用于处理与用户相关操作
 */
@Controller
public class UserController {
    private static File userDir;//用来表示存放所有用户信息的目录
    static {
        userDir = new File("./users");
        if(!userDir.exists()){
            userDir.mkdirs();
        }
    }

    @RequestMapping("/loginUser")
    public void login(HttpServletRequest request, HttpServletResponse response){
        System.out.println("开始处理登录!!!");
        String username = request.getParameter("username");
        String password = request.getParameter("password");
        System.out.println(username+","+password);
        //必要的验证工作
        if(username==null||username.trim().isEmpty()||
                password==null||password.trim().isEmpty()){
            response.sendRedirect("login_info_error.html");
            return;
        }

        File file = new File(userDir,username+".obj");
        if(file.exists()){//用户名是否存在(是否为一个注册用户)
            try (
                    FileInputStream fis = new FileInputStream(file);
                    ObjectInputStream ois = new ObjectInputStream(fis);
            ){
                User user = (User)ois.readObject();//读取回来的是注册用户信息
                //比较登录的密码和该注册用户的密码是否一致
                if(user.getPassword().equals(password)){
                    //登录成功
                    response.sendRedirect("/login_success.html");
                    return;
                }
            } catch (IOException | ClassNotFoundException e) {
                e.printStackTrace();
            }
        }

        //登录失败
        response.sendRedirect("/login_fail.html");
    }

    @RequestMapping("/regUser")
    public void reg(HttpServletRequest request, HttpServletResponse response){
        System.out.println("开始处理用户注册!!!");
        String username = request.getParameter("username");
        String password = request.getParameter("password");
        String nickname = request.getParameter("nickname");
        String ageStr = request.getParameter("age");
        System.out.println(username+","+password+","+nickname+","+ageStr);
        if(username==null||username.trim().isEmpty()||
           password==null||password.trim().isEmpty()||
           nickname==null||nickname.trim().isEmpty()||
           ageStr==null||ageStr.trim().isEmpty()||
           !ageStr.matches("[0-9]+")
        ){
            //信息输入有误提示页面
            response.sendRedirect("/reg_info_error.html");
            return;
        }

        System.out.println(username+","+password+","+nickname+","+ageStr);

        int age = Integer.parseInt(ageStr);
        //2
        User user = new User(username,password,nickname,age);
        //参数1:userDir表示父目录 参数2:userDir目录下的子项
        File file = new File(userDir,username+".obj");
        if(file.exists()){//文件存在则说明该用户已经注册过了
            response.sendRedirect("/have_user.html");
            return;
        }
        try (
                FileOutputStream fos = new FileOutputStream(file);
                ObjectOutputStream oos = new ObjectOutputStream(fos);
        ){
            oos.writeObject(user);
            //利用响应对象要求浏览器访问注册成功页面
            response.sendRedirect("/reg_success.html");
        } catch (IOException e) {
            e.printStackTrace();
        }


    }
}
