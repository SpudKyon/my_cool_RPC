package com.dongdong.test.server;

import com.dongdong.rpc.core.server.SocketServer;
import com.dongdong.rpc.core.service.impl.GreetingServiceImpl;
import com.dongdong.rpc.core.service.impl.StatusServiceImpl;

import java.util.logging.Logger;

public class SocketServerTest {

  public static void main(String[] args) {
    SocketServer server = new SocketServer(9090);
    server.addService(GreetingServiceImpl.class.getInterfaces()[0].getName(), new GreetingServiceImpl());
    server.addService(StatusServiceImpl.class.getInterfaces()[0].getName(), new StatusServiceImpl());
    server.start();
  }
}
