package com.huiyuan2.cloud.basic.network;

import com.huiyuan2.cloud.basic.utils.NamedThreadFactory;
import io.netty.bootstrap.ServerBootstrap;
import io.netty.buffer.PooledByteBufAllocator;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelFutureListener;
import io.netty.channel.ChannelOption;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.epoll.EpollServerSocketChannel;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import io.netty.util.ResourceLeakDetector;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections4.CollectionUtils;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * @description: 网络服务端
 * @author： 灰原二
 * @date: 2022/9/26 7:41
 */
@Slf4j
public class NetServer {

    private final String name;
    private final EventLoopGroup boss;
    private final EventLoopGroup worker;
    private final boolean supportEpoll;
    private final DefaultScheduler defaultScheduler;
    private BaseChannelInitializer baseChannelInitializer;

    public NetServer(String name, DefaultScheduler defaultScheduler) {
        this(name, false, defaultScheduler, 0);
    }

    public NetServer(String name, boolean supportEpoll, DefaultScheduler defaultScheduler, int workerThreads) {
        this.name = name;
        this.supportEpoll = supportEpoll;
        this.defaultScheduler = defaultScheduler;
        this.boss = new NioEventLoopGroup(0, new NamedThreadFactory("NetServer-Boss-", false));
        this.worker = new NioEventLoopGroup(workerThreads, new NamedThreadFactory("NetServer-Worker-", false));
        this.baseChannelInitializer = new BaseChannelInitializer();
    }

    public void addHandlers(List<AbstractChannelHandler> handlers) {
        if (CollectionUtils.isEmpty(handlers)) {
            return;
        }
        baseChannelInitializer.addHandlers(handlers);
    }

    /**
     * 绑定端口，同步等待关闭
     *
     * @param port
     *
     * @throws InterruptedException
     */
    public void bind(int port) throws InterruptedException {
        bind(Collections.singletonList(port));
    }


    /**
     * 绑定多个端口
     *
     * @param ports
     *
     * @throws InterruptedException
     */
    public void bind(List<Integer> ports) throws InterruptedException {
        internalBind(ports);
    }

    /**
     * 异步绑定端口
     *
     * @param port
     */
    public void bindAsync(int port) {
        defaultScheduler.scheduleOnce("绑定服务端口", () -> {
            try {
                internalBind(Collections.singletonList(port));
            } catch (InterruptedException e) {
                log.info("NetServer internalBind is Interrupted");
            }
        }, 0);
    }

    /**
     * 绑定端口
     *
     * @param ports
     *
     * @throws InterruptedException
     */
    private void internalBind(List<Integer> ports) throws InterruptedException {

        try {
            ServerBootstrap bootstrap = new ServerBootstrap();
            bootstrap.group(boss, worker)
                    .channel(supportEpoll ? EpollServerSocketChannel.class : NioServerSocketChannel.class)
                    .option(ChannelOption.ALLOCATOR, PooledByteBufAllocator.DEFAULT)
                    .childOption(ChannelOption.ALLOCATOR, PooledByteBufAllocator.DEFAULT)
                    .childHandler(baseChannelInitializer);

            ResourceLeakDetector.setLevel(ResourceLeakDetector.Level.ADVANCED);
            List<ChannelFuture> channelFutures = new ArrayList<>();

            for (int port : ports) {
                ChannelFuture future = bootstrap.bind(port).sync();
                log.info("Netty Server started on port:{}", port);
                channelFutures.add(future);
            }

            for (ChannelFuture channelFuture : channelFutures) {
                channelFuture.channel().closeFuture().addListener((ChannelFutureListener) future -> future.channel().close());
            }
            for (ChannelFuture channelFuture : channelFutures) {
                channelFuture.channel().closeFuture().sync();
            }
        } finally {
            boss.shutdownGracefully();
            worker.shutdownGracefully();
        }

    }
}
