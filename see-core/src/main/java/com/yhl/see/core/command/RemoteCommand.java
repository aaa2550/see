package com.yhl.see.core.command;

import com.yhl.see.core.files.FileNode;
import lombok.Data;

import java.io.Serializable;
import java.util.List;

/**
 * Created by yanghailong on 2018/9/4.
 */
@Data
public class RemoteCommand implements Serializable {

    private int type;
    private String className;
    private String methodName;
    private String packageName;
    private List<FileNode> fileNodes;

    public RemoteCommand(int type) {
        this.type = type;
    }

}
