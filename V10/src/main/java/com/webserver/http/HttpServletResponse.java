package com.webserver.http;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.net.Socket;
import java.nio.charset.StandardCharsets;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

public class HttpServletResponse {
    private Socket socket;
    private int statusCode = 200;       //状态代码
    private String statusReason = "ok"; //状态描述

    //响应头相关信息
    private Map<String,String> headers = new HashMap<>();


    //响应正文相关信息
    private File contentFile;

    public HttpServletResponse(Socket socket) {
        this.socket = socket;
    }

    public int getStatusCode() {
        return statusCode;
    }

    public void setStatusCode(int statusCode) {
        this.statusCode = statusCode;
    }

    public String getStatusReason() {
        return statusReason;
    }

    public void setStatusReason(String statusReason) {
        this.statusReason = statusReason;
    }

    public File getContentFile() {
        return contentFile;
    }

    public void setContentFile(File contentFile) {
        this.contentFile = contentFile;
    }

    /**
     * 将当前响应对象内容按照标准的响应格式发送给客户端
     */
    public void response() throws IOException {
        //发送状态行
        sendStatusLine();
        //发送响应头
        sendHeaders();
        //发送响应正文
        sendContent();
    }
    //发送状态行
    private void sendStatusLine() throws IOException {
        println("HTTP/1.1" + " " + statusCode + " " + statusReason);
    }

    //发送响应头
    private void sendHeaders() throws IOException {
//        println("Content-Type: text/html");
//        println("Content-Length: " + contentFile.length());
        /*
        使用Set集合 entry 方法将Map集合转换
        便利获取key和value
        拼接line字符串
        发送消息
         */
        Set<Map.Entry<String,String>> entrySet = headers.entrySet();
        for (Map.Entry<String,String> e :entrySet){
            String key = e.getKey();
            String value = e.getValue();
            String line = key+": "+value;
            println(line);
        }

        //发送空行
        println("");
    }

    private void sendContent() throws IOException {
        OutputStream os = socket.getOutputStream();
        FileInputStream fis = new FileInputStream(contentFile);
        byte[] buf = new byte[1024 * 10];
        int len;
        while ((len = fis.read(buf)) != -1) {
            os.write(buf, 0, len);
        }
    }



    private void println(String line) throws IOException {
        OutputStream os = socket.getOutputStream();
        byte[] data = line.getBytes(StandardCharsets.ISO_8859_1);
        os.write(data);
        os.write(13);
        os.write(10);
    }


    public void addHeader(String name,String value){
        headers.put(name,value);
    }

}
