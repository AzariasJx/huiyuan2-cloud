package com.huiyuan2.cloud.mq.consumer;

/**
 * @description:
 * @author： 灰原二
 * @date: 2022/9/24 9:23
 */
public interface MessageListener {

    /**
     * 处理消息
     * @param message
     */
    default void onMessage(String message){
        onMessage(message,()->{});
    }

    /**
     *
     * @param message
     * @param acknowledgement 提交offset，处理完以后一定要提交
     */
    void onMessage(String message,Acknowledgement acknowledgement);
}
