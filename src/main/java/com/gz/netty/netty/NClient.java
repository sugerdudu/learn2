package com.gz.netty.netty;

import io.netty.bootstrap.Bootstrap;
import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioSocketChannel;
import io.netty.handler.codec.string.StringDecoder;
import io.netty.handler.codec.string.StringEncoder;
import io.netty.util.CharsetUtil;
import io.netty.util.NettyRuntime;

import java.nio.charset.Charset;
import java.util.Scanner;

public class NClient {
    public static void main(String[] args) throws InterruptedException {

        System.out.println(NettyRuntime.availableProcessors() * 2);

        EventLoopGroup mainGroup = new NioEventLoopGroup(1);
        try {
            init(mainGroup);
        } catch (Throwable e) {
            throw e;
        } finally {
            mainGroup.shutdownGracefully();
        }
    }

    private static void init(EventLoopGroup mainGroup) throws InterruptedException {
        Bootstrap bootstrap = new Bootstrap();
        bootstrap.group(mainGroup)
                .channel(NioSocketChannel.class)
                .handler(new ChannelInitializer<SocketChannel>() {
                    @Override
                    protected void initChannel(SocketChannel ch) throws Exception {
                        ch.pipeline().addLast("decoder", new StringDecoder());
                        ch.pipeline().addLast("encoder", new StringEncoder());
                        ch.pipeline().addLast(new NClientHandler());
                    }
                });

        System.out.println("netty client started");

        ChannelFuture cf = bootstrap.connect("127.0.0.1",7174).sync();


        Scanner scanner = new Scanner(System.in);
        /*while (scanner.hasNextLine()) {
            String s = scanner.nextLine();
            cf.channel().writeAndFlush(s);
//            ByteBuf buf = Unpooled.copiedBuffer(s.getBytes());
//            cf.channel().writeAndFlush(buf);
        }*/

        for (int i = 0; i < 200; i++) {
            cf.channel().writeAndFlush("天天，你好");
        }


        cf.channel().closeFuture().sync();
    }
}
