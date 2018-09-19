package com.yhl.see.console.aop;

import com.yhl.see.core.aop.AspectExecutor;
import com.yhl.see.core.command.RemoteCommand;
import io.netty.channel.ChannelHandlerContext;
import javassist.CtClass;
import javassist.NotFoundException;

/**
 * Created by yanghailong on 2018/9/4.
 */
public class LogAspectExecutor extends AspectExecutor {

    @Override
    public void execute(ChannelHandlerContext ctx, RemoteCommand command) {
        try {
            //TODO 执行逻辑
            CtClass clazz = CLASS_POOL.getCtClass(command.getClassName());
            clazz.getDeclaredMethod(command.getMethodName());
        } catch (NotFoundException e) {
            e.printStackTrace();
        }
    }
}
