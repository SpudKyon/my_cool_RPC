package com.dongdong.rpc.core.utils;

import com.dongdong.rpc.common.cs.RPCServer;
import com.dongdong.rpc.core.NacosServiceRegistry;
import com.dongdong.rpc.core.server.ServiceMap;

public class ServiceShutdownHook {

  private static volatile ServiceShutdownHook instance;

  private ServiceShutdownHook() {
  }

  public static ServiceShutdownHook getInstance() {
    if (instance == null) {
      synchronized (ServiceShutdownHook.class) {
        if (instance == null) {
          instance = new ServiceShutdownHook();
        }
      }
    }
    return instance;
  }

  public void clearRegisteredService(RPCServer server) {
    Runtime.getRuntime().addShutdownHook(new Thread(() -> {
      ServiceMap.getInstance().popAllService(NacosServiceRegistry.getInstance(), server);
    }));
  }
}
