package com.gz.netty.netty;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import io.netty.util.CharsetUtil;

public class NClientHandler extends ChannelInboundHandlerAdapter {


    @Override
    public void channelActive(ChannelHandlerContext ctx) throws Exception {
//        System.out.println("channelActive:"+Thread.currentThread().getName());
//        ByteBuf buf = Unpooled.copiedBuffer("你好 server".getBytes());
//        ctx.writeAndFlush(buf);
    }

    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
//        System.out.println("channelRead"+Thread.currentThread().getName());
//        ByteBuf buf = (ByteBuf) msg;
//        System.out.println(buf.toString(CharsetUtil.UTF_8));
        System.out.println(msg);
    }

    @Override
    public void channelReadComplete(ChannelHandlerContext ctx) throws Exception {
//        System.out.println("channelReadComplete:"+Thread.currentThread().getName());
//        Thread.sleep(1000);
//        ByteBuf buf = Unpooled.copiedBuffer("你好 server2".getBytes());
//        ctx.writeAndFlush(buf);
    }

    @Override
    public void channelInactive(ChannelHandlerContext ctx) throws Exception {
        System.out.println("channelInactive");
        ctx.connect(ctx.channel().remoteAddress()).sync();
    }
}
