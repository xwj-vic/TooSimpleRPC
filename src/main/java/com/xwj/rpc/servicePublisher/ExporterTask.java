package com.xwj.rpc.servicePublisher;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.net.Socket;

/**
 * 将客户端（调用者）发送过来的流数据反序列化成对象，并反射调用指定执行的服务方法
 * 获取结果后，将结果序列化传回给客户端
 */
public class ExporterTask implements Runnable {

    Socket client = null;

    public ExporterTask(Socket accept) {
        this.client = accept;
    }

    @Override
    public void run() {
        ObjectInputStream input = null;
        ObjectOutputStream output = null;

        try {
            input = new ObjectInputStream(client.getInputStream());
            String interFaceName = input.readUTF();
            Class<?> service = Class.forName(interFaceName); //获取类的字节码
            String methodName = input.readUTF(); //方法名
            Class<?>[] parameterTypes = (Class<?>[])input.readObject(); //参数类型
            Object[] arguments = (Object[])input.readObject(); //参数
            Method method = service.getMethod(methodName, parameterTypes); //获取指定方法
            Object result = method.invoke(service.newInstance(), arguments); //执行指定方法；

            output = new ObjectOutputStream(client.getOutputStream());
            output.writeObject(result);//响应代理层

        } catch (IOException | ClassNotFoundException | NoSuchMethodException | InstantiationException | IllegalAccessException | InvocationTargetException e) {
            e.printStackTrace();
        } finally {
            if (output != null) {
                try {
                    output.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            if (input != null) {
                try {
                    input.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            if(client != null) {
                try {
                    client.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }
}
