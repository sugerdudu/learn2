package com.gz.netty.netty;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import io.netty.channel.group.ChannelGroup;
import io.netty.channel.group.DefaultChannelGroup;
import io.netty.util.CharsetUtil;
import io.netty.util.concurrent.ImmediateEventExecutor;

public class NServerHandler extends ChannelInboundHandlerAdapter {

    static ChannelGroup channelGroup = new DefaultChannelGroup(ImmediateEventExecutor.INSTANCE);

    @Override
    public void channelActive(ChannelHandlerContext ctx) throws Exception {
        String msg = ctx.channel().remoteAddress() + " 上线了";
        channelGroup.writeAndFlush(msg);

//        ByteBuf buf = Unpooled.copiedBuffer((msg).getBytes());
//        channelGroup.writeAndFlush(buf);

        channelGroup.add(ctx.channel());

        System.out.println("新用户:"+ctx.channel().remoteAddress());

    }

    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
        //System.out.println("channelRead:"+Thread.currentThread().getName());
        System.out.println("channelRead:"+Thread.currentThread().getName());
//        ByteBuf buf = (ByteBuf) msg;

        String s = ctx.channel().remoteAddress() + ": " + msg;
//        ByteBuf send = Unpooled.copiedBuffer(s.getBytes());
        channelGroup.writeAndFlush(s);
    }

    @Override
    public void channelReadComplete(ChannelHandlerContext ctx) throws Exception {
       // System.out.println("channelReadComplete:"+Thread.currentThread().getName());
//        ByteBuf buf = Unpooled.copiedBuffer("你好 client".getBytes());
//        ctx.writeAndFlush(buf);
    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
        System.out.println("exceptionCaught:"+Thread.currentThread().getName());
        cause.printStackTrace();
        ctx.close();
    }
}
