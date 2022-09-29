package com.huiyuan2.cloud.basic.utils;

/**
 * @description: 常量
 * @author： 灰原二
 * @date: 2022/9/26 22:02
 */
public class Constants {

    /**
     * netty传输的最大字节数
     */
    public static final  int MAX_BYTES = 10*1024*1024;

    /**
     * 分块传输 每一块的大小
     */
    public static final int CHUNKED_SIZE = (int) (MAX_BYTES*0.5F);

    public static final String REGISTRY_TYPE = "cloud-registry";
    public static final String CONFIG_CENTER_TYPE = "cloud-config-center";

    public static final int SLOTS_COUNT = 16384;
}
