package com.webserver.core;

import java.io.IOException;
import java.io.InputStream;
import java.net.Socket;
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
            String line =readLine();
            String method;
            String uri;
            String protocol;

            //读取
            String[] lines= line.split("\\s");
            method=lines[0];
            uri=lines[1];
            protocol=lines[2];

            Map<String,String> headers = new HashMap<>();
            while(true){
                line = readLine();
                if (line.isEmpty()){
                    break;
                }
                lines = line.split(":\\s");
                headers.put(lines[0],lines[1]);
            }
            System.out.println(headers);

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

    private String readLine() throws IOException {
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
        return line;
    }
}
