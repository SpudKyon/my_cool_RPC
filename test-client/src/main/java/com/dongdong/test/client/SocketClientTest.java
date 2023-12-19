package com.dongdong.test.client;

import com.dongdong.rpc.common.RequestParam;
import com.dongdong.rpc.common.service.GreetingService;
import com.dongdong.rpc.common.service.StatusService;
import com.dongdong.rpc.core.proxy.RPCStaticProxy;
import com.dongdong.rpc.core.service.impl.SS;

public class SocketClientTest {
  public static void main(String[] args) {
    RPCStaticProxy proxy = new RPCStaticProxy("localhost", 9090);
    GreetingService greetingService = proxy.getProxy(GreetingService.class);
    System.out.println(greetingService.sayHello(1));
    System.out.println(greetingService.sayHello(new RequestParam<>("hello", String.class)));
    for (int i = 0; i < 20; i++) {
      System.out.println(greetingService.sayHello(i));
    }
    StatusService statusService = proxy.getProxy(StatusService.class);
    System.out.println(statusService.getStatus());
    System.out.println(statusService.setStatus("running"));
    System.out.println(statusService.getStatus());
    System.out.println(proxy.getProxy(SS.class).run());
  }
}
