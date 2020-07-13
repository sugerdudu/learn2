package com.gz.netty;

import java.io.IOException;
import java.net.Socket;

public class BioClient {
    public static void main(String[] args) throws IOException {
        Socket socket = new Socket("127.0.0.1", 7171);
        socket.getOutputStream().write(("cgq " + System.currentTimeMillis()).getBytes());
        socket.getOutputStream().flush();

        byte[] bytes = new byte[1024];
        int read = socket.getInputStream().read(bytes);
        System.out.println(new String(bytes, 0, read));
        socket.close();
    }
}
