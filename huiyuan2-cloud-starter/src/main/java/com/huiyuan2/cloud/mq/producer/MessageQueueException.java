package com.huiyuan2.cloud.mq.producer;

/**
 * @description:
 * @author： 灰原二
 * @date: 2022/9/24 9:08
 */
public class MessageQueueException extends Exception{
    public MessageQueueException() {
    }

    public MessageQueueException(String message) {
        super(message);
    }

    public MessageQueueException(Exception e) {
        super(e);
    }
}
