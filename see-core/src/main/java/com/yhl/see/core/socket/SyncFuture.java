package com.yhl.see.core.socket;

import java.util.concurrent.CountDownLatch;

/**
 * Created by yanghailong on 2018/9/27.
 */
public class SyncFuture<T> {

    private CountDownLatch countDownLatch;
    private T t;

    public SyncFuture() {
        countDownLatch = new CountDownLatch(1);
    }

    public void callback(T t) {
        this.t = t;
        countDownLatch.countDown();
    }

    public T get() {
        try {
            countDownLatch.await();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        return t;
    }

}
