package com.yhl.see.core.seriallizer;

/**
 */
public class NettySerializationUtils {

    public static final byte[] EMPTY_ARRAY = new byte[0];

    public static NettySerializer<Object> serializer = new JacksonSerializer();

    public static boolean isEmpty(byte[] data) {
        return (data == null || data.length == 0);
    }
}
