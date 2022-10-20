package com.webserver.http;

import java.io.IOException;
import java.io.InputStream;
import java.net.Socket;
import java.util.HashMap;
import java.util.Map;

public class HttpServletRequest {


    private String method;
    private String uri;
    private String protocol;
    private Socket socket;
    Map<String, String> headers = new HashMap<>();


    public String getMethod() {
        return method;
    }


    public String getUri() {
        return uri;
    }


    public String getProtocol() {
        return protocol;
    }

    public String getHeader(String name) {
        return headers.get(name);
    }

    public HttpServletRequest(Socket socket) throws IOException {
        this.socket = socket;
        //解析请求行
        parseRequestLine();
        //解析消息头
        parseHeaders();
        //解析消息正文
        parseContent();

    }

    //解析请求行
    private void parseRequestLine() throws IOException {
        String line = readLine();
        //读取
        String[] lines = line.split("\\s");
        method = lines[0];
        uri = lines[1];
        protocol = lines[2];
        System.out.println("method:"+method);//method:GET
        System.out.println("uri:"+uri);//uri:/index.html
        System.out.println("protocol:"+protocol);//protocol:HTTP/1.1
    }

    //解析消息头
    private void parseHeaders() throws IOException {
        while (true) {
            String line = readLine();
            if (line.isEmpty()) {
                break;
            }
            String[] lines = line.split(":\\s");
            headers.put(lines[0], lines[1]);
        }
        System.out.println(headers);
    }

    //解析消息正文
    private void parseContent() {
    }

    //读取请求数据
    private String readLine() throws IOException {
        InputStream in = socket.getInputStream();
        int d;
        StringBuilder builder = new StringBuilder();
        char pre = 'a', cur = 'a';
        while ((d = in.read()) != -1) {
            cur = (char) d;
            builder.append(cur);
            if (pre == 13 && cur == 10) {
                break;
            }
            pre = cur;
        }
        String line = builder.toString().trim();
        return line;
    }


}
