package com.yhl.see.console.common;

import io.netty.channel.Channel;

import java.util.concurrent.ConcurrentHashMap;

/**
 * Created by yanghailong on 2018/9/20.
 */
public class AppManager {

    private static final ConcurrentHashMap<String, Channel> appAddressChannelMap = new ConcurrentHashMap<>();

    public static Channel put(String address, Channel channel) {
        return appAddressChannelMap.put(address, channel);
    }

    public static Channel get(String address) {
        return appAddressChannelMap.get(address);
    }

}
