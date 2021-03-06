package com.yhl.see.core.socket;

import com.yhl.see.core.command.RemoteCommand;
import com.yhl.see.core.command.RemoteEnum;
import com.yhl.see.core.seriallizer.NettySerializationUtils;
import com.yhl.see.core.util.PushUtil;
import io.netty.bootstrap.Bootstrap;
import io.netty.channel.*;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioSocketChannel;
import io.netty.handler.codec.http.HttpClientCodec;
import io.netty.handler.codec.http.HttpObjectAggregator;
import io.netty.handler.codec.http.websocketx.CloseWebSocketFrame;
import io.netty.handler.timeout.IdleStateHandler;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;
import java.rmi.Remote;
import java.util.concurrent.TimeUnit;

@Component
public class SocketClient {

    @Value("${sockte.ip}")
    private String host;
    @Value("${sockte.port}")
    private int port;
    private static Channel ch;
    private static final EventLoopGroup group = new NioEventLoopGroup();
    private static volatile Boolean SEND_SWITCH;
    private static int tryTimes;

    @PostConstruct
    public void init() {
        try {
            open();
        } catch (Exception e) {
            e.printStackTrace();
            temporaryStopSend();
        }
    }

    private void open() throws InterruptedException {
        Bootstrap b = new Bootstrap();
        b.group(group).channel(NioSocketChannel.class)
                .option(ChannelOption.CONNECT_TIMEOUT_MILLIS, 10500)
                .handler(new ChannelInitializer<SocketChannel>() {
                    @Override
                    public void initChannel(SocketChannel ch) {
                        ChannelPipeline pipeline = ch.pipeline();
                        ch.pipeline().addLast(new ClientDecoderHandler(Integer.MAX_VALUE,
                                0, 4, 0, 0, false));
                        pipeline.addLast(new IdleStateHandler(0,14,0, TimeUnit.SECONDS));
                        pipeline.addLast(new ServerHandler());
                    }
                });
        ch = b.connect(host, port).sync().channel();
        SEND_SWITCH = true;
        //发起注册
        eval(new RemoteCommand(RemoteEnum.注册.getType()));
        ch.closeFuture().sync();

    }

    @PreDestroy
    public void close() throws InterruptedException {
        ch.writeAndFlush(new CloseWebSocketFrame());
        ch.closeFuture().sync();
        group.shutdownGracefully();
    }

    public void eval(RemoteCommand command) {
        if (SEND_SWITCH) {
            if (ch != null && ch.isOpen()) {
                /*byte[] body = NettySerializationUtils.serializer.serialize(command);
                ch.writeAndFlush(body);*/
                PushUtil.pushMsg(command, ch);
            } else {
                SEND_SWITCH = false;
                try {
                    //重新连接
                    open();
                    tryTimes = 0;
                } catch (Exception e) {
                    temporaryStopSend();
                }
            }
        }
    }

    /**
     * 10秒检测重连
     */
    private void temporaryStopSend() {
        /*SEND_SWITCH = false;
        if (tryTimes++ < 5) {
            log.info("10秒后尝试重新连接web socket,times:{}", tryTimes);
            ThreadUtil.submit(()-> {
                try {
                    Thread.sleep(10000);
                    SEND_SWITCH = true;
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            });
        }else{
            LocalCacheSwitch.SOCKET_SWITCH = false;
            SEND_SWITCH = true;
            tryTimes = 0;
            MessageSmsUtil.sendMsgWhenException("webSocket 连接失败");
            log.info("尝试多次失败，放弃连接");
        }*/

    }
}