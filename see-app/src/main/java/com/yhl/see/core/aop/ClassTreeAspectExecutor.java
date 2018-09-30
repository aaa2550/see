package com.yhl.see.core.aop;

import com.yhl.see.core.command.RemoteCommand;
import com.yhl.see.core.files.FileNode;
import com.yhl.see.core.files.PackageUtil;
import com.yhl.see.core.seriallizer.NettySerializationUtils;
import com.yhl.see.core.util.PushUtil;
import io.netty.channel.ChannelHandlerContext;
import javassist.CtClass;
import javassist.NotFoundException;

import java.util.List;

/**
 * Created by yanghailong on 2018/9/4.
 */
public class ClassTreeAspectExecutor extends AspectExecutor {

    @Override
    public void execute(ChannelHandlerContext ctx, RemoteCommand command) {
        try {
            List<FileNode> fileNodes = PackageUtil.getClassName(command.getPackageName());
            command.setFileNodes(fileNodes);
            PushUtil.pushMsg(command.reversal(), ctx.channel());
        } catch (Throwable e) {
            e.printStackTrace();
        }
    }
}
