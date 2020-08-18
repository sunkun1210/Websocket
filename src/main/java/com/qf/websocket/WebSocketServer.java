package com.qf.websocket;

import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.*;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import io.netty.handler.codec.http.HttpObjectAggregator;
import io.netty.handler.codec.http.HttpServerCodec;
import io.netty.handler.codec.http.websocketx.WebSocketServerProtocolHandler;
import io.netty.handler.codec.string.StringDecoder;
import io.netty.handler.codec.string.StringEncoder;
import io.netty.handler.timeout.ReadTimeoutHandler;

import java.util.concurrent.TimeUnit;

public class WebSocketServer {

    public static void main(String[] args) {
        EventLoopGroup master = new NioEventLoopGroup();
        EventLoopGroup salve = new NioEventLoopGroup();
        try {
            ServerBootstrap bootstrap = new ServerBootstrap();
            bootstrap.group(master,salve);
            bootstrap.channel(NioServerSocketChannel.class);
            bootstrap.childHandler(new ChannelInitializer() {
                @Override
                protected void initChannel(Channel channel) throws Exception {
                    ChannelPipeline pipeline = channel.pipeline();


                    pipeline.addLast(new HttpServerCodec()); // HttpRequset
                    pipeline.addLast(new HttpObjectAggregator(1024*10)); // FullHttpReqeust

                    // 添加WebSocket解编码
                    pipeline.addLast(new WebSocketServerProtocolHandler("/"));

                    // 客户端10s不发送信息自动断开
                    pipeline.addLast(new ReadTimeoutHandler(10, TimeUnit.SECONDS));

                    // 添加处理客户端的请求的处理器
                    pipeline.addLast(new WebSocketChannelHanlder());

                }
            });
            ChannelFuture channelFuture = bootstrap.bind(8080);
            channelFuture.sync();
            System.out.println("服务端启动成功");
        } catch (InterruptedException e) {
            e.printStackTrace();
        }finally {
//            master.shutdownGracefully();
//            salve.shutdownGracefully();
        }
    }
}
