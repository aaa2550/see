package com.yhl.see.core.util;

import com.yhl.see.core.seriallizer.NettySerializationUtils;
import io.netty.buffer.ByteBuf;
import io.netty.channel.Channel;
import io.netty.channel.ChannelFuture;
import io.netty.util.concurrent.Future;
import io.netty.util.concurrent.GenericFutureListener;

public class PushUtil {

    public static <T> void pushMsg(T obj, Channel ctx) {
        pushMsg(obj, ctx, null);
    }

    public static <T> void pushMsg(T obj, Channel ctx, GenericFutureListener<? extends Future<? super Void>> listener) {
        byte[] body = NettySerializationUtils.serializer.serialize(obj);
        int length = body.length;
        byte[] all = new byte[body.length + 4];
        System.arraycopy(CodecUtil.int2bytes(length), 0, all, 0, 4);
        System.arraycopy(body, 0, all, 4, body.length);
        ByteBuf encoded = ctx.alloc().buffer(all.length);
        encoded.writeBytes(all);
        ChannelFuture channelFuture = ctx.writeAndFlush(encoded);
        if (listener != null) {
            channelFuture.addListener(listener);
        }
    }
}
