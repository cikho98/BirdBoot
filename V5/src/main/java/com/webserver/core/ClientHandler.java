package com.webserver.core;

import com.webserver.http.HttpServletRequest;

import java.io.*;
import java.net.Socket;
import java.net.URISyntaxException;
import java.nio.charset.StandardCharsets;
import java.util.HashMap;
import java.util.Map;

public class ClientHandler implements Runnable {
    private Socket socket;

    public ClientHandler(Socket socket) {
        this.socket = socket;
    }

    @Override
    public void run() {
        try {
            HttpServletRequest request = new HttpServletRequest(socket);
            String path = request.getUri();

            File root = new File(
                    ClientHandler.class.getClassLoader().getResource(".").toURI()
            );
            File staticDir = new File(root, "static");
            File file = new File(staticDir, path);

            //发送状态行
            println("HTTP/1.1 200 ok");
            //发送响应头
            println("Content-Type: text/html");
            println( "Content-Length: " + file.length());
            //发送空行
            println("");
            //发送响应正文
            OutputStream os = socket.getOutputStream();
            FileInputStream fis = new FileInputStream(file);
            byte[] buf = new byte[1024 * 10];
            int len;
            while ((len = fis.read(buf)) != -1) {
                os.write(buf, 0, len);
            }
        } catch (IOException | URISyntaxException e) {
            e.printStackTrace();
        } finally {
            try {
                socket.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

    }

    private void println(String line) throws IOException {
        OutputStream os = socket.getOutputStream();
        byte[] data = line.getBytes(StandardCharsets.ISO_8859_1);
        os.write(data);
        os.write(13);
        os.write(10);
    }

}
