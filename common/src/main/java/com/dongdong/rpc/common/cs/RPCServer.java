package com.dongdong.rpc.common.cs;

public interface RPCServer {

    boolean addService(String serviceName, Object service);

    void start();

    void stop();
}
