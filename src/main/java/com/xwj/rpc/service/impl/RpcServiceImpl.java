package com.xwj.rpc.service.impl;

import com.xwj.rpc.service.interfaces.RpcService;

/**
 * 模拟一个服务，供服务消费者进调用
 */
public class RpcServiceImpl implements RpcService {
    @Override
    public String hello(String name) {
        return "hello " + name + " 欢迎你来调用我";
    }
}
