package com.gz.netty;

import com.gz.MathUtil;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.nio.channels.ServerSocketChannel;
import java.nio.channels.SocketChannel;
import java.util.Iterator;

public class NioServer {
    public static void main(String[] args) throws IOException {
        ServerSocketChannel channel = ServerSocketChannel.open();
        channel.configureBlocking(false);
        channel.socket().bind(new InetSocketAddress(7172));

        Selector selector = Selector.open();
        channel.register(selector, SelectionKey.OP_ACCEPT);

        while (true) {
            System.out.println("服务端 等待事件");
            selector.select();
            System.out.println("服务端 有事件");
            Iterator<SelectionKey> it = selector.selectedKeys().iterator();
            while (it.hasNext()) {
                SelectionKey key = it.next();
                it.remove();
                handle(key);
            }
        }
    }

    private static void handle(SelectionKey key) throws IOException {
        System.out.println("----------------- handler");
        System.out.println("isReadable:"+key.isReadable()
                +" , isConnectable:"+key.isConnectable()
                +" , isAcceptable:"+key.isAcceptable()
                +" , isWritable:"+key.isWritable()
                +" , isConnectable:"+key.isConnectable()
        );


        if (key.isAcceptable()) {
            System.out.println("服务端 客户端 连接事件 isAcceptable ");
            ServerSocketChannel channel = (ServerSocketChannel)key.channel();
            //accept是一个阴塞方式，但在连接已经发生后且isAcceptable=true，调用accept跟非阻塞一样效果，会立即执行完成
            SocketChannel sc = channel.accept();
            sc.configureBlocking(false);
            sc.register(key.selector(), SelectionKey.OP_READ);
        } else if (key.isReadable()) {
            System.out.println("服务端 客户端 可读事件 isReadable");
            SocketChannel sc = (SocketChannel)key.channel();
            ByteBuffer buffer = ByteBuffer.allocate(1024);

            //nio的read方法是非阻塞的，在调用isReadable后，已经有读取事件发生
            int len = sc.read(buffer);
            long receiveTime = 0;
            if (len != -1) {
                String receive = new String(buffer.array(), 0, len);
                receiveTime = Long.parseLong(receive);
                System.out.println("服务端 读取到数据：" + receive);
            }

//            ByteBuffer bufferWrite = ByteBuffer.wrap(("服务端 收到啦 " + System.currentTimeMillis()).getBytes());
            long time = System.nanoTime();

            System.out.println("用时" + MathUtil.divideNanoToMilli(time - receiveTime) + " ms");

            ByteBuffer bufferWrite = ByteBuffer.wrap(String.valueOf(time).getBytes());
            sc.write(bufferWrite);
            key.interestOps(SelectionKey.OP_READ | SelectionKey.OP_WRITE);
            sc.close();
        }
//        System.out.println(Thread.currentThread().getName());
    }
}
