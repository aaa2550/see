package com.yhl.see.core.aop;

import com.yhl.see.core.command.RemoteCommand;
import io.netty.channel.ChannelHandlerContext;
import javassist.ClassPool;

/**
 * Created by yanghailong on 2018/9/4.
 */
public abstract class AspectExecutor {

    public static final ClassPool CLASS_POOL = ClassPool.getDefault();

    public abstract void execute(ChannelHandlerContext ctx, RemoteCommand command);

}
