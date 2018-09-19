package com.yhl.see.console.aop;

import com.yhl.see.core.aop.AspectExecutor;
import com.yhl.see.core.command.RemoteCommand;
import io.netty.channel.Channel;
import io.netty.channel.ChannelHandlerContext;

import java.util.concurrent.ConcurrentHashMap;

/**
 * Created by yanghailong on 2018/9/4.
 */
public class RegisterAspectExecutor extends AspectExecutor {

    private static final ConcurrentHashMap<String, Channel> appAddressChannelMap = new ConcurrentHashMap<>();

    @Override
    public void execute(ChannelHandlerContext ctx, RemoteCommand command) {
        Channel channel = ctx.channel();
        appAddressChannelMap.put(channel.remoteAddress().toString(), channel);
    }
}
