package com.yhl.see.console.socket;

import com.yhl.see.core.socket.ServerDecoderHandler;
import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelOption;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import io.netty.handler.timeout.IdleStateHandler;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;
import javax.annotation.Resource;
import java.net.InetSocketAddress;
import java.util.concurrent.TimeUnit;

@Service
public class SocketServer {

    private static final Logger logger = LoggerFactory.getLogger(SocketServer.class);

    @Value("${sockte.ip}")
    private String socketIp;

    @Value("${sockte.port}")
    private int socketPort;

    @Resource
    private ServerHandler serviceHandler;

    @PostConstruct
    public void start() throws InterruptedException {
        exportService();
    }

    private void exportService() {
        ServerBootstrap serverBootstrap = new ServerBootstrap();
        EventLoopGroup eventExecutors = new NioEventLoopGroup();
        try {
            serverBootstrap.group(eventExecutors);
            serverBootstrap.channel(NioServerSocketChannel.class);
            serverBootstrap.localAddress(new InetSocketAddress(socketIp, socketPort));
            serverBootstrap.childHandler(new ChannelInitializer<SocketChannel>() {
                @Override
                protected void initChannel(SocketChannel socketChannel) throws Exception {
                    socketChannel.pipeline().addLast(new ServerDecoderHandler(Integer.MAX_VALUE,
                            0, 4, 0, 0, false));
                    socketChannel.pipeline().addLast(new IdleStateHandler(
                            15, 0, 0, TimeUnit.SECONDS));
                    socketChannel.pipeline().addLast(serviceHandler);
                }
            }).childOption(ChannelOption.SO_KEEPALIVE, true);
            ChannelFuture channelFuture = serverBootstrap.bind().sync();
            logger.info("{} started and listen on {}", SocketServer.class.getName(), channelFuture.channel().localAddress());
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}
