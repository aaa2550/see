package com.yhl.see.console.controller;

import com.yhl.see.console.common.AppManager;
import com.yhl.see.core.command.ApiResponse;
import com.yhl.see.core.command.RemoteCommand;
import com.yhl.see.core.command.RemoteEnum;
import com.yhl.see.core.util.PushUtil;
import io.netty.channel.Channel;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;

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
        PushUtil.pushMsg(command, appChannel);
        return null;
    }

}
