package com.dongdong.rpc.common;

import java.net.InetSocketAddress;

public interface ServiceRegistry {
  void register(String serviceName, InetSocketAddress address);
  InetSocketAddress lookupService(String serviceName);
  void unregister(String serviceName, InetSocketAddress address);
}
