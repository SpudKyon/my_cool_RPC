package com.dongdong.test.server;

import com.dongdong.rpc.core.codec.CoolDecoder;
import com.dongdong.rpc.core.codec.CoolEncoder;
import com.dongdong.rpc.core.serializer.JsonSerializer;
import com.dongdong.rpc.core.server.NettyServer;
import com.dongdong.rpc.core.server.ServiceMap;
import com.dongdong.rpc.core.service.impl.GreetingServiceImpl;
import com.dongdong.rpc.core.service.impl.StatusServiceImpl;
import lombok.extern.slf4j.Slf4j;

import java.security.Provider;

@Slf4j
public class NettyServerTest {
  public static void main(String[] args) {
    NettyServer<CoolDecoder, CoolEncoder<JsonSerializer>>  server = new NettyServer<>(8080, new CoolDecoder(), new CoolEncoder<>(new JsonSerializer()));
    ServiceMap serviceMap = ServiceMap.getInstance();
    serviceMap.addService(GreetingServiceImpl.class.getInterfaces()[0].getName(), new GreetingServiceImpl());
    serviceMap.addService(StatusServiceImpl.class.getInterfaces()[0].getName(), new StatusServiceImpl());
    server.start();
  }
}
