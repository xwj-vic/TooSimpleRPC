package com.xwj.rpc.servicePublisher;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.net.ServerSocket;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;

/**
 * 服务发布者
 */
public class ServicePublisher {
//    初始化一个固定大小的线程池
    public static Executor executor = Executors.newFixedThreadPool(Runtime.getRuntime().availableProcessors());

    public static void exporter(String hostName, int port) throws IOException {
        ServerSocket server = new ServerSocket();
        server.bind(new InetSocketAddress(hostName, port)); //绑定客户端TCP连接
        try {
            while (true) {
                executor.execute(new ExporterTask(server.accept())); //收到客户端的请求后，封装成Task放到线程池里执行
            }
        } finally {
            server.close(); //服务调用完成，释放socket资源
        }
    }
}
