package com.qf.websocket;

import io.netty.channel.Channel;
import io.netty.channel.ChannelHandler;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import io.netty.handler.codec.http.websocketx.TextWebSocketFrame;
import io.netty.util.concurrent.EventExecutorGroup;

public class WebSocketChannelHanlder extends SimpleChannelInboundHandler<TextWebSocketFrame>{
    @Override
    protected void channelRead0(ChannelHandlerContext channelHandlerContext, TextWebSocketFrame textWebSocketFrame) throws Exception {
        String text = textWebSocketFrame.text();
        System.out.println("读取客户端的内容："+text);

        // 判断消息是否是心跳
        if("heard".equals(text)){
            TextWebSocketFrame resp = new TextWebSocketFrame("heard");
            channelHandlerContext.writeAndFlush(resp);
            return;
        }

        TextWebSocketFrame resp = new TextWebSocketFrame("嗯！");
//        channelHandlerContext.channel().writeAndFlush(resp);
        channelHandlerContext.writeAndFlush(resp);

        Channel channel = channelHandlerContext.channel();
        System.out.println(channel);
    }

    @Override
    public void channelRegistered(ChannelHandlerContext ctx) throws Exception {
        System.out.println("新客户端连接。。。");
    }

    @Override
    public void channelUnregistered(ChannelHandlerContext ctx) throws Exception {
        System.out.println("客户断开连接。。。");
    }
}
