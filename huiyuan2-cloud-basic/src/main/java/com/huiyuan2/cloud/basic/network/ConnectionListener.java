package com.huiyuan2.cloud.basic.network;

/**
 * @description: 网络连接状态监听器
 * @author： 灰原二
 * @date: 2022/9/26 21:51
 */
public interface ConnectionListener {

    /**
     * 网络状态监听
     * @param connected 是否连接
     * @throws Exception
     */
    void onConnectStatusChanged(boolean connected) throws Exception;
}
