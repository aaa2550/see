package com.yhl.see.core.socket;

import com.yhl.see.core.aop.AspectExecutorFactory;
import com.yhl.see.core.command.RemoteCommand;
import com.yhl.see.core.command.RemoteEnum;
import io.netty.channel.ChannelHandler;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import io.netty.handler.timeout.IdleState;
import io.netty.handler.timeout.IdleStateEvent;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@ChannelHandler.Sharable
public class ServerHandler extends ChannelInboundHandlerAdapter {

    private static final Logger logger = LoggerFactory.getLogger(ServerHandler.class);

    @Override
    public void channelActive(ChannelHandlerContext ctx) throws Exception {
        RemoteCommand remoteCommand = new RemoteCommand(RemoteEnum.注册.getType());
        ctx.channel().writeAndFlush(remoteCommand);
        logger.info("channel connected, remoteIp={}", ctx.channel().remoteAddress().toString());
    }

    /**
     * 链接断开
     *
     * @param ctx
     * @throws Exception
     */
    @Override
    public void channelInactive(ChannelHandlerContext ctx) throws Exception {
        logger.info("channel closed, remoteIp={}", ctx.channel().remoteAddress().toString());
    }

    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) {
        try {
            RemoteCommand command = (RemoteCommand) msg;
            if (command.getType() > 0) {
                AspectExecutorFactory.create(command).execute(ctx, command);
            } else {
                System.out.println("处理响应");
            }
        } catch (Throwable e) {
            e.printStackTrace();
        }
    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
        logger.error("Socket Exception = {}", cause);
        ctx.close();//出现异常时关闭channel
    }

    @Override
    public void userEventTriggered(ChannelHandlerContext ctx, Object evt) throws Exception {
        if (evt instanceof IdleStateEvent) {
            if (((IdleStateEvent) evt).state() == IdleState.ALL_IDLE) {
                logger.info("channel idle closed, remoteIp = {}", ctx.channel().remoteAddress().toString());
                ctx.writeAndFlush("biubiu");
            }
        } else {
            super.userEventTriggered(ctx, evt);
        }
    }
}
