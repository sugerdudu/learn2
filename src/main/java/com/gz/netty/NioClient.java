package com.gz.netty;

import com.gz.MathUtil;
import io.netty.buffer.ByteBuf;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.net.Socket;
import java.nio.ByteBuffer;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.nio.channels.SocketChannel;
import java.util.Iterator;

public class NioClient {
    Selector selector;
    public static void main(String[] args) throws IOException, InterruptedException {
        NioClient client = new NioClient();
        client.initClient("127.0.0.1", 7172);
        client.connect();
    }

    private void connect() throws IOException, InterruptedException {
        while (true) {
            selector.select();
           // System.out.println("connect");
            Iterator<SelectionKey> it = selector.selectedKeys().iterator();
            while (it.hasNext()) {
                SelectionKey key = it.next();
                it.remove();

                handler(key);
            }
        }
    }

    private void handler(SelectionKey key) throws IOException, InterruptedException {
        System.out.println("----------------- handler");
       // Thread.sleep(3000);
        if (key.isConnectable()) {
            SocketChannel sc = (SocketChannel) key.channel();
            if (sc.isConnectionPending()) {
                sc.finishConnect();
            }
            sc.configureBlocking(false);

//            ByteBuffer buffer = ByteBuffer.wrap(("cgq " + System.currentTimeMillis()).getBytes());
            ByteBuffer buffer = ByteBuffer.wrap(String.valueOf(System.nanoTime()).getBytes());
            sc.write(buffer);
            sc.register(this.selector, SelectionKey.OP_READ);

        } else if (key.isReadable()) {
            System.out.println("客户端 可读事件 isReadable");
            SocketChannel sc = (SocketChannel)key.channel();
            ByteBuffer buffer = ByteBuffer.allocate(1024);

            //nio的read方法是非阻塞的，在调用isReadable后，已经有读取事件发生
            int len = sc.read(buffer);
            long receiveTime = 0;
            if (len != -1) {
                String receive = new String(buffer.array(), 0, len);
                receiveTime = Long.parseLong(receive);
                System.out.println("客户端 读取到数据：" + receive);
            }

            long time = System.nanoTime();
            System.out.println("用时" + MathUtil.divideNanoToMilli( time-receiveTime) + " ms");


//            ByteBuffer bufferWrite = ByteBuffer.wrap(("客户端 收到啦 " + System.currentTimeMillis()).getBytes());
//            sc.write(bufferWrite);
            key.interestOps(SelectionKey.OP_READ | SelectionKey.OP_WRITE);
            sc.close();
        }
    }

    public void initClient(String ip, int port) throws IOException {
        SocketChannel sc = SocketChannel.open();

        sc.configureBlocking(false);

        this.selector = Selector.open();

        sc.connect(new InetSocketAddress(ip, port));
        sc.register(this.selector, SelectionKey.OP_CONNECT);

    }
}
