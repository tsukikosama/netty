package org.netty.myEcho;

import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioServerSocketChannel;

import java.net.InetSocketAddress;

public class EchoServer {
    private static int prot;

    public EchoServer(int prot) {
        this.prot = 8080;
    }

    public static void main(String[] args) throws Exception {

            new EchoServer(prot).start();

    }

    public void start() throws Exception {
        final EchoServerHandler handler = new EchoServerHandler();
        //创建EventLoopGroup
        EventLoopGroup boos = new NioEventLoopGroup();
        try {
            //创建ServerBootstrap 引导和绑定服务器
            ServerBootstrap b = new ServerBootstrap();
            b.group(boos)
                    //指定所使用的nio传输channel
                    .channel(NioServerSocketChannel.class)
                    //使用指定的端口设置套接字地址
                    .localAddress(new InetSocketAddress(prot))
                    //添加一个子EchoServerHandler到子channel的pipeline上
                    .childHandler(new ChannelInitializer<SocketChannel>() {
                         @Override
                         protected void initChannel(SocketChannel ch) throws Exception {
                             //标记shareable总是可以使用同样的实例
                             ch.pipeline().addLast(handler);
                         }
                    });
            ChannelFuture future = b.bind().sync();
            future.channel().closeFuture().sync();
        }finally {
            //关闭eventloopgroup释放所有资源
            boos.shutdownGracefully().sync();
        }
    }
}
