package com.webserver.http;

import java.io.IOException;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;
import java.net.Socket;
import java.net.URLDecoder;
import java.nio.charset.StandardCharsets;
import java.util.HashMap;
import java.util.Map;

/**
 * 请求对象
 * 该类的每一个实例用于表示HTTP协议规定的请求内容。每个请求由三部分构成
 * 请求行，消息头，消息正文
 */
public class HttpServletRequest {
    private Socket socket;

    //请求行相关信息
    private String method;//请求方式
    private String uri;//抽象路径
    private String protocol;//协议版本

    private String requestURI;//uri中的请求部分，即:"?"左侧内容
    private String queryString;//uri中的参数部分，即:"?"右侧内容
    private Map<String,String> parameters = new HashMap<>();//每一组参数


    //消息头相关信息
    private Map<String,String> headers = new HashMap<>();


    /**
     * 构造器，用于初始化请求对象，初始化的过程就是读取浏览器发送过来的请求。
     */
    public HttpServletRequest(Socket socket) throws IOException, EmptyRequestException {
        this.socket = socket;
        //1解析请求行
        parseRequestLine();
        //2解析消息头
        parseHeaders();
        //3解析消息正文
        parseContent();
    }
    //解析请求行
    private void parseRequestLine() throws IOException, EmptyRequestException {
        String line = readLine();

        if(line.isEmpty()){//如果请求行为空字符串则本次为空请求
            throw new EmptyRequestException();
        }

        System.out.println("请求行内容:"+line);
        //将请求行内容按照空格拆分为三部分，分别初始化三个变量
         String[] data= line.split("\\s");
        method = data[0];
        uri = data[1];
        protocol = data[2];
        parseURI();//进一步解析uri
        System.out.println("method:"+method);//method:GET
        System.out.println("uri:"+uri);//uri:/index.html
        System.out.println("protocol:"+protocol);//protocol:HTTP/1.1
    }
    //进一步解析uri
    private void parseURI(){
        String[] data = uri.split("\\?");
        requestURI = data[0];
        if(data.length>1){
            queryString = data[1];
            parseParameters(queryString);
        }

        //测试提交reg.html注册表单后，这里的打桩应该看到:
        System.out.println("requestURI:"+requestURI);//requestURI:/regUser
        System.out.println("queryString:"+queryString);//username=fancq&password=&nickname=chuanqi&age=22
        System.out.println("parameters:"+parameters);//parameters:{username=xxx, xxx=xxx,...}
    }

    private void parseParameters(String line){
        try {
            line = URLDecoder.decode(line,"UTF-8");
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        //将参数部分按照"&"拆分出每一组参数
        String[] data = line.split("&");
        //遍历每一组参数
        for(String para : data){
            String[] paras = para.split("=",2);
            parameters.put(paras[0], paras[1]);
        }
    }

    //解析消息头
    private void parseHeaders() throws IOException {
        while(true) {
            String line = readLine();
            if(line.isEmpty()){//读取到了空行(消息头发送完毕了)
                break;
            }
            System.out.println("消息头:" + line);
            String[] data = line.split(":\\s");
            headers.put(data[0],data[1]);
        }
        System.out.println("headers:"+headers);
    }
    //解析消息正文
    private void parseContent() throws IOException {
        //判断消息头中是否含有说明正文长度的头:Content-Length
        if(headers.containsKey("Content-Length")){
            //获取正文长度
            int contentLength = Integer.parseInt(
                headers.get("Content-Length")
            );
            InputStream in = socket.getInputStream();
            byte[] contentData = new byte[contentLength];
            in.read(contentData);

            //根据消息头Content-Type来解析正文
            String contentType = headers.get("Content-Type");
            if("application/x-www-form-urlencoded".equals(contentType)){
                String line = new String(contentData, StandardCharsets.ISO_8859_1);
                System.out.println("正文内容:"+line);
                parseParameters(line);
            }
//            else if(){}//后期可以继续扩展判断其它类型并进行处理,比如附件上传的

        }
    }


    private String readLine() throws IOException {
        InputStream in = socket.getInputStream();
        //实现读取一行字符串的操作,便于我们读取请求行和消息头
        int d;
        StringBuilder builder = new StringBuilder();
        char pre='a',cur='a';//pre表示上次读取的字符,cur表示本次读取的字符
        while((d = in.read())!=-1){
            cur = (char)d;//本次读取到的字符
            if(pre==13 && cur==10){//判断是否连续读取到了回车+换行
                break;
            }
            builder.append(cur);
            pre = cur;//进入下次循环前,将本次读取的字符记作上次读取的字符
        }
        return builder.toString().trim();
    }

    public String getMethod() {
        return method;
    }

    public String getUri() {
        return uri;
    }

    public String getProtocol() {
        return protocol;
    }

    /**
     * 根据消息头的名字获取该消息头对应的值
     * @param name
     * @return
     */
    public String getHeader(String name) {
        return headers.get(name);
    }

    public String getRequestURI() {
        return requestURI;
    }

    public String getQueryString() {
        return queryString;
    }

    public String getParameter(String name) {
        return parameters.get(name);
    }
}



