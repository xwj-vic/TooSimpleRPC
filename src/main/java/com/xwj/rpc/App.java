package com.xwj.rpc;

import com.xwj.rpc.proxy.RpcProxy;
import com.xwj.rpc.service.impl.RpcServiceImpl;
import com.xwj.rpc.service.interfaces.RpcService;
import com.xwj.rpc.servicePublisher.ServicePublisher;

import java.io.IOException;
import java.net.InetSocketAddress;

public class App
{

    public static final String serverPublisherHostName = "localhost";

    public static final int port = 8088;

    public static void main( String[] args )
    {
        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    ServicePublisher.exporter(serverPublisherHostName, port); //开启一个子线程，模拟启动服务提供者
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }).start();


        /**
         * 模拟服务调用者 调用RpcService,通过代理来调用
         */
        RpcProxy<RpcService> serviceRpcProxy = new RpcProxy<>();
        RpcService rpcService = serviceRpcProxy.proxyInstance(RpcServiceImpl.class, new InetSocketAddress(serverPublisherHostName, port));
        String result = rpcService.hello("三胖");
        System.out.println("rpc调用结果：\n" + result);

    }
}
