package com.webserver.http;

import javax.activation.MimetypesFileTypeMap;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.net.Socket;
import java.nio.charset.StandardCharsets;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

/**
 * 响应对象
 * 当前类的每一个实例用于表示HTTP协议的一个响应
 * 每个响应由三部分构成:
 * 状态行，响应头，响应正文
 */
public class HttpServletResponse {
    private static MimetypesFileTypeMap mime = new MimetypesFileTypeMap();

    private Socket socket;
    //状态行相关信息
    private int statusCode = 200;           //状态代码
    private String statusReason = "OK";     //状态描述

    //响应头相关信息
    private Map<String,String> headers = new HashMap<>();

    //响应正文相关信息
    private File contentFile;               //响应正文对应的实体文件


    public HttpServletResponse(Socket socket){
        this.socket = socket;
    }

    /**
     * 将当前响应对象内容按照标准的响应格式发送给客户端
     */
    public void response() throws IOException {
        //1发送状态行
        sendStatusLine();
        //2发送响应头
        sendHeaders();
        //3发送响应正文
        sendContent();
    }
    private void sendStatusLine() throws IOException {
        println("HTTP/1.1"+" "+statusCode+" "+statusReason);
    }
    private void sendHeaders() throws IOException {
        /*
            headers
            key             value
            Content-Type    text/html
            Content-Length  12123
            Server          BirdWebServer
            xxxxx           xxxxx
         */
        Set<Map.Entry<String,String>> entrySet = headers.entrySet();
        for(Map.Entry<String,String> e : entrySet){
            String key = e.getKey();
            String value = e.getValue();
            //Content-Type: text/html
            String line = key+": "+value;
            println(line);
        }
        //单独发送空行表示响应头部分发送完毕
        println("");
    }
    private void sendContent() throws IOException {
        OutputStream out = socket.getOutputStream();
        if(contentFile!=null) {
            FileInputStream fis = new FileInputStream(contentFile);
            byte[] buf = new byte[1024 * 10];
            int len;
            while ((len = fis.read(buf)) != -1) {
                out.write(buf, 0, len);
            }
        }
    }


    private void println(String line) throws IOException {
        OutputStream out = socket.getOutputStream();
        byte[] data = line.getBytes(StandardCharsets.ISO_8859_1);
        out.write(data);
        out.write(13);
        out.write(10);
    }

    /**
     * 要求浏览器重定向到指定路径
     * @param uri
     */
    public void sendRedirect(String uri){
        //1将状态代码设置为302
        statusCode=302;
        statusReason="Moved Temporarily";
        //2添加响应头Location
        addHeader("Location",uri);
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
        //根据正文文件添加响应头Content-Type和Content-Length

        addHeader("Content-Type",mime.getContentType(contentFile));
        addHeader("Content-Length",contentFile.length()+"");

    }

    /**
     * 添加一个要发送的响应头
     * @param name
     * @param value
     */
    public void addHeader(String name,String value){
        headers.put(name,value);
    }
}
