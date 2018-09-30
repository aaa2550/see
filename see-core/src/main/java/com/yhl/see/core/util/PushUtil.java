package com.yhl.see.core.util;

import com.yhl.see.core.command.RemoteCommand;
import com.yhl.see.core.seriallizer.NettySerializationUtils;
import io.netty.buffer.ByteBuf;
import io.netty.channel.Channel;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelPromise;
import io.netty.util.concurrent.Future;
import io.netty.util.concurrent.GenericFutureListener;

import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.CountDownLatch;

public class PushUtil {

    private static ConcurrentHashMap<String, CountDownLatch> countDownLatchConcurrentHashMap = new ConcurrentHashMap<>();
    private static ConcurrentHashMap<String, RemoteCommand> remoteCommandConcurrentHashMap = new ConcurrentHashMap<>();

    public static void release(String requestId, RemoteCommand command) {
        remoteCommandConcurrentHashMap.put(requestId, command);
        countDownLatchConcurrentHashMap.get(requestId).countDown();
    }

    public static void pushMsg(RemoteCommand command, Channel ctx) {
        byte[] body = NettySerializationUtils.serializer.serialize(command);
        int length = body.length;
        byte[] all = new byte[body.length + 4];
        System.arraycopy(CodecUtil.int2bytes(length), 0, all, 0, 4);
        System.arraycopy(body, 0, all, 4, body.length);
        ByteBuf encoded = ctx.alloc().buffer(all.length);
        encoded.writeBytes(all);
        ctx.writeAndFlush(encoded);
    }

    public static RemoteCommand syncPushMsg(RemoteCommand command, Channel ctx) {
        byte[] body = NettySerializationUtils.serializer.serialize(command);
        int length = body.length;
        byte[] all = new byte[body.length + 4];
        System.arraycopy(CodecUtil.int2bytes(length), 0, all, 0, 4);
        System.arraycopy(body, 0, all, 4, body.length);
        ByteBuf encoded = ctx.alloc().buffer(all.length);
        encoded.writeBytes(all);
        CountDownLatch countDownLatch = new CountDownLatch(1);
        countDownLatchConcurrentHashMap.put(command.getRequestId(), countDownLatch);
        ctx.writeAndFlush(encoded);
        try {
            countDownLatch.await();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        return remoteCommandConcurrentHashMap.remove(command.getRequestId());
    }

}
