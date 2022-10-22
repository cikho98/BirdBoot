package com.webserver.test;

import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.nio.charset.StandardCharsets;

public class URLDecoderDemo {
    public static void main(String[] args) throws UnsupportedEncodingException {
        String line = "/regUser?username=%E5%88%98%E5%BE%B7%E5%8D%8E&password=123&nickname=1231&age=13";
        line = URLDecoder.decode(line,"UTF-8");
        System.out.println(line);
    }
}
