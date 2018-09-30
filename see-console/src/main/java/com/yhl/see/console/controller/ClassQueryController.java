package com.yhl.see.console.controller;

import com.yhl.see.console.common.AppManager;
import com.yhl.see.core.command.ApiResponse;
import com.yhl.see.core.command.RemoteCommand;
import com.yhl.see.core.command.RemoteEnum;
import com.yhl.see.core.socket.SyncFuture;
import com.yhl.see.core.util.PushUtil;
import io.netty.channel.Channel;
import io.netty.channel.ChannelFuture;
import io.netty.util.concurrent.Future;
import io.netty.util.concurrent.GenericFutureListener;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;
import java.util.concurrent.CountDownLatch;

/**
 * Created by yanghailong on 2018/9/20.
 */
@RestController
@RequestMapping("/class")
public class ClassQueryController {

    /**
     * 获取孩子
     * @return
     */
    @RequestMapping(value = "/getChildren")
    public ApiResponse getChildren(String address, String clazzPackage) {
        Channel appChannel = AppManager.get(address);
        RemoteCommand command = new RemoteCommand(RemoteEnum.查询类树.getType());
        command.setPackageName(clazzPackage);
        return ApiResponse.success(PushUtil.syncPushMsg(command, appChannel));
    }

}
