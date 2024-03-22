package org.netty.myEcho;

import io.netty.bootstrap.Bootstrap;
import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioSocketChannel;

import java.net.InetSocketAddress;

public class EchoClient {
    public static  String host;
    public static  int port;

    public EchoClient(String host, int port) {
        this.host = "localhost";
        this.port = 8080;
    }

    public void start() throws  Exception{
        EventLoopGroup boss = new NioEventLoopGroup();
        try {
            //创建boostrap
            Bootstrap bootstrap = new Bootstrap();
            //指定EventLoopGroup
            bootstrap.group(boss)
                    //使用nio传输的channel
                    .channel(NioSocketChannel.class)
                    .remoteAddress(new InetSocketAddress(host, port))
                    //在创建channel的时候添加一个echoClientHandler实力
                    .handler(new ChannelInitializer<SocketChannel>() {

                        @Override
                        protected void initChannel(SocketChannel ch) throws Exception {
                            ch.pipeline().addLast(new EchoClientHandler());
                        }
                    });
            ChannelFuture future = bootstrap.connect().sync();
            future.channel().closeFuture();
        }finally {
            boss.shutdownGracefully().sync();

        }
    }

    public static void main(String[] args) throws Exception {

        new EchoClient(host, port).start();
    }
}
