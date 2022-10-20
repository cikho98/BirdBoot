package com.webserver.core;

import java.io.IOException;
import java.io.InputStream;
import java.net.Socket;

public class ClientHandler implements Runnable {
    private Socket socket;

    public ClientHandler(Socket socket) {
        this.socket = socket;
    }

    @Override
    public void run() {
        try {
            InputStream in = socket.getInputStream();
            int d;
            StringBuilder builder = new StringBuilder();
            char pre='a',cur='a';
            while ((d = in.read()) != -1) {
                cur = (char) d;
                builder.append(cur);
                if (pre==13 && cur ==10){
                    break;
                }
                pre=cur;
            }
            String line = builder.toString().trim();
            System.out.println(line);

            String method;
            String uri;
            String protocol;

            String[] lines= line.split("\\s");
            method=lines[0];
            uri=lines[1];
            protocol=lines[2];
            System.out.println(method);
            System.out.println(uri);
            System.out.println(protocol);



        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            try {
                socket.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

    }
}
