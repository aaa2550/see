package com.yhl.see.core.command;

import com.yhl.see.core.files.FileNode;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.util.List;
import java.util.UUID;

/**
 * Created by yanghailong on 2018/9/4.
 */
@Data
@NoArgsConstructor
public class RemoteCommand implements Serializable {

    private String requestId;
    private int type;
    private String className;
    private String methodName;
    private String packageName;
    private List<FileNode> fileNodes;

    public RemoteCommand(int type) {
        this.requestId = UUID.randomUUID().toString();
        this.type = type;
    }

    public RemoteCommand reversal() {
        type = ~type + 1;
        return this;
    }

    public boolean isRequest() {
        return type > 0;
    }

}
