package com.dongdong.test.server;

import com.dongdong.rpc.core.server.SocketServer;
import com.dongdong.rpc.core.service.impl.GreetingServiceImpl;

import java.util.logging.Logger;

public class SocketServerTest {

  public static void main(String[] args) {
    SocketServer server = new SocketServer(9090);
    GreetingServiceImpl greetingService = new GreetingServiceImpl();
    server.addService(GreetingServiceImpl.class.getInterfaces()[0].getName(), greetingService);
    server.start();
  }
}
