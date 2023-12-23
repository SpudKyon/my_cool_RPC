package com.dongdong.rpc.core.server;

import java.util.concurrent.ConcurrentHashMap;

public class ServiceMap extends ConcurrentHashMap<String, Object>{


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
}
