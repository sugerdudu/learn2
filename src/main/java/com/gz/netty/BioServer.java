package com.gz.netty;


import java.io.IOException;
import java.net.Socket;

public class BioServer {
    public static void main(String[] args) throws IOException {
        java.net.ServerSocket serverSocket = new java.net.ServerSocket(7171);
        while (true) {
            System.out.println("等待连接");
            Socket socket = serverSocket.accept();
            System.out.println("有客户端进来");
            new Thread(() -> {
                try {
                    handler(socket);
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }).start();
        }
    }

    private static void handler(Socket socket) throws IOException {
        System.out.println(Thread.currentThread().getName());

        byte[] bytes = new byte[1024];
        int read = socket.getInputStream().read(bytes);

        System.out.println("read = " + read);

        System.out.println(new String(bytes,0,read));

        socket.getOutputStream().write(new String("接收到了 " + System.currentTimeMillis()).getBytes());
        socket.getOutputStream().flush();
    }
}
