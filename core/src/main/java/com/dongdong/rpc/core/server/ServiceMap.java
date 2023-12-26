package com.dongdong.rpc.core.server;

import com.dongdong.rpc.common.ServiceRegistry;
import com.dongdong.rpc.common.cs.RPCServer;

import java.net.InetSocketAddress;
import java.util.concurrent.ConcurrentHashMap;

public class ServiceMap extends ConcurrentHashMap<String, Object> {

  private static volatile ServiceMap instance;

  private ServiceMap() {
    super();
  }

  public static ServiceMap getInstance() {
    if (instance == null) {
      synchronized (ServiceMap.class) {
        if (instance == null) {
          instance = new ServiceMap();
        }
      }
    }
    return instance;
  }

  public boolean addService(String serviceName, Object service) {
    if (super.containsKey(serviceName)) {
      return false;
    }
    super.put(serviceName, service);
    return true;
  }

  public void pushAllService(ServiceRegistry registry, RPCServer server) {
    InetSocketAddress address = new InetSocketAddress(server.getPort());
    for (String serviceName : super.keySet()) {
      registry.register(serviceName, address);
    }
  }

  public void popAllService(ServiceRegistry registry, RPCServer server) {
    InetSocketAddress address = new InetSocketAddress(server.getPort());
    for (String serviceName : super.keySet()) {
      registry.unregister(serviceName, address);
    }
  }
}
