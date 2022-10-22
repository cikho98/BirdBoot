package com.webserver.controller;

import com.webserver.annoations.Controller;
import com.webserver.annoations.RequestMapping;
import com.webserver.http.HttpServletRequest;
import com.webserver.http.HttpServletResponse;
@Controller
public class ArticleController {
    @RequestMapping("/writeArticle")
    public void writeArticle(HttpServletRequest request, HttpServletResponse response){

    }
}
