package com.yhl.see.core.util;

import com.yhl.see.core.seriallizer.NettySerializationUtils;
import io.netty.buffer.ByteBuf;
import io.netty.channel.Channel;

public class PushUtil {

    public static <T> void pushMsg(T obj, Channel ctx) {
        byte[] body = NettySerializationUtils.serializer.serialize(obj);
        int length = body.length;
        byte[] all = new byte[body.length + 4];
        System.arraycopy(CodecUtil.int2bytes(length), 0, all, 0, 4);
        System.arraycopy(body, 0, all, 4, body.length);
        ByteBuf encoded = ctx.alloc().buffer(all.length);
        encoded.writeBytes(all);
        ctx.writeAndFlush(encoded);
    }
}
