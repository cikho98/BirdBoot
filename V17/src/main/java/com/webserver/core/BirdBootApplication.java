package com.webserver.core;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.concurrent.Executor;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * 该项目用来模拟Tomcat+SpringMVC来搭建网络应用
 * 这里我们会实现Tomcat做一个WebServer与客户端进行HTTP交互
 * 并模拟SpringMVC的主要工作完成对业务的处理
 */
public class BirdBootApplication {
    private ServerSocket serverSocket;
    private ExecutorService threadPool;

    public BirdBootApplication(){
        try {
            System.out.println("正在启动服务端...");
            serverSocket = new ServerSocket(8088);
            threadPool = Executors.newFixedThreadPool(50);
            System.out.println("服务端启动完毕!");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void start(){
        try {
            while (true) {
                System.out.println("等待客户端连接");
                Socket socket = serverSocket.accept();
                System.out.println("一个客户端连接了!");
                //启动一个线程来处理与该客户端的交互
                ClientHandler handler = new ClientHandler(socket);
                threadPool.execute(handler);
            }

        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    public static void main(String[] args) {
        BirdBootApplication application = new BirdBootApplication();
        application.start();
    }
}
