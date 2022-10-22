package com.webserver.core.controller;

import com.webserver.annoations.Controller;
import com.webserver.annoations.RequestMapping;
import com.webserver.http.HttpServletRequest;
import com.webserver.http.HttpServletResponse;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.InputStream;

@Controller
public class ArticleController {

    private static File articleDir;

    static {
        articleDir=new File("./articleDir");
    }

    @RequestMapping("/writeArticle")
    public void writeArticle(HttpServletRequest request, HttpServletResponse response) throws FileNotFoundException {
        System.out.println("开始写入文章");
        String title= request.getParameter("title");
        String author = request.getParameter("author");
        String text = request.getParameter("text");

        File article = new File(articleDir,title+".obj");
        FileOutputStream fos = new FileOutputStream(article);


    }
}
