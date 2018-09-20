package com.yhl.see.core.seriallizer;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.yhl.see.core.exception.NettySerializationException;

/**
 */
public class JacksonSerializer implements NettySerializer<Object> {

    private ObjectMapper objectMapper = new ObjectMapper();

    public JacksonSerializer() {
        objectMapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
    }

    @Override
    public byte[] serialize(Object t) throws NettySerializationException {
        if (t == null) {
            return NettySerializationUtils.EMPTY_ARRAY;
        }

        try {
            return this.objectMapper.writeValueAsBytes(t);
        } catch (Exception e) {
            throw new NettySerializationException("Could not write JSON: " + e.getMessage(), e);
        }
    }

    @Override
    public <T> T deserialize(byte[] bytes, Class<T> clz) throws NettySerializationException {
        if (NettySerializationUtils.isEmpty(bytes)) {
            return null;
        }

        try {
            return this.objectMapper.readValue(bytes, 0, bytes.length, clz);
        } catch (Exception e) {
            throw new NettySerializationException("Could not read JSON: " + e.getMessage(), e);
        }
    }

    public <T> T deserialize(byte[] bytes, TypeReference<T> tr) throws NettySerializationException {
        if (NettySerializationUtils.isEmpty(bytes)) {
            return null;
        }

        try {
            return this.objectMapper.readValue(bytes, tr);
        } catch (Exception e) {
            throw new NettySerializationException("Could not read JSON: " + e.getMessage(), e);
        }
    }
}
