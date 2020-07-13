package com.gz.netty;

import io.netty.buffer.ByteBuf;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.AsynchronousServerSocketChannel;
import java.nio.channels.AsynchronousSocketChannel;
import java.nio.channels.CompletionHandler;

public class AioServer {

    public static void main(String[] args) throws IOException {
        AsynchronousServerSocketChannel serverSocketChannel =
                AsynchronousServerSocketChannel.open().bind(new InetSocketAddress(7173));

        serverSocketChannel.accept(null, new CompletionHandler<AsynchronousSocketChannel, Object>() {
            @Override
            public void completed(AsynchronousSocketChannel socketChannel, Object o) {
                serverSocketChannel.accept(o, this);
                ByteBuffer buffer = ByteBuffer.allocate(1024);
                socketChannel.read(buffer, buffer, new CompletionHandler<Integer, ByteBuffer>() {
                    @Override
                    public void completed(Integer result, ByteBuffer buffer) {
                        buffer.flip();
                        System.out.println(new String(buffer.array(),0,result));
                        socketChannel.write(ByteBuffer.wrap("收到".getBytes()));
                    }

                    @Override
                    public void failed(Throwable e, ByteBuffer byteBuffer) {
                        throw new RuntimeException(e);
                    }
                });
            }

            @Override
            public void failed(Throwable throwable, Object o) {
                System.out.println("failed");
            }
        });

        System.in.read();
    }
}
