package com.yhl.see.console.aop;

import com.yhl.see.console.common.AppManager;
import com.yhl.see.core.aop.AspectExecutor;
import com.yhl.see.core.command.RemoteCommand;
import com.yhl.see.core.util.PushUtil;
import io.netty.channel.Channel;
import io.netty.channel.ChannelHandlerContext;

import java.util.concurrent.ConcurrentHashMap;

/**
 * Created by yanghailong on 2018/9/4.
 */
public class RegisterAspectExecutor extends AspectExecutor {

    @Override
    public void execute(ChannelHandlerContext ctx, RemoteCommand command) {
        Channel channel = ctx.channel();
        AppManager.appAddressChannelMap.put(channel.remoteAddress().toString(), channel);
        PushUtil.pushMsg(command.reversal(), channel);
    }
}
