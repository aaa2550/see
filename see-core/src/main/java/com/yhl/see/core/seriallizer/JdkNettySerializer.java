package com.yhl.see.core.seriallizer;

import com.yhl.see.core.exception.NettySerializationException;

import java.io.*;

/**
 * Created by yanghailong on 2018/9/4.
 */
public class JdkNettySerializer implements NettySerializer {

    @Override
    public byte[] serialize(Object o) throws NettySerializationException {
        try (ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
             ObjectOutputStream serializer = new ObjectOutputStream(new ByteArrayOutputStream())) {
            serializer.writeObject(o);
            return byteArrayOutputStream.toByteArray();
        } catch (IOException e) {
            throw new NettySerializationException(e.getMessage());
        }
    }

    @Override
    public Object deserialize(byte[] bytes, Class clz) throws NettySerializationException {
        Object obj = null;
        try (ObjectInputStream serializer = new ObjectInputStream(new ByteArrayInputStream(bytes))) {
            obj = serializer.readObject();
        } catch (EOFException ignored) {
        } catch (IOException | ClassNotFoundException e) {
            e.printStackTrace();
            throw new NettySerializationException(e.getMessage());
        }
        return obj;
    }
}
