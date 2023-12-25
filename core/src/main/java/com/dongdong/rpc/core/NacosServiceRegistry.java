package com.dongdong.rpc.core;

import com.alibaba.nacos.api.naming.NamingFactory;
import com.alibaba.nacos.api.naming.NamingService;
import com.alibaba.nacos.api.naming.pojo.Instance;
import com.dongdong.rpc.common.ServiceRegistry;
import com.dongdong.rpc.common.exception.RPCException;
import lombok.extern.slf4j.Slf4j;

import java.net.InetSocketAddress;
import java.util.Properties;

@Slf4j
public class NacosServiceRegistry implements ServiceRegistry {

  private final NamingService namingService;
  private final String serverAddr = "127.0.0.1:8848";
  private final String userName = "nacos";
  private final String pasword = "nacos";
  private static volatile NacosServiceRegistry instance;

  private NacosServiceRegistry() {
    try {
      Properties properties = new Properties();
      properties.put("serverAddr", serverAddr);
      properties.put("username", userName);
      properties.put("password", pasword);
      namingService = NamingFactory.createNamingService(properties);
    } catch (Exception e) {
      log.error("occur exception:", e);
      throw new RPCException(e.getMessage(), e);
    }
  }

  public static NacosServiceRegistry getInstance() {
    if (instance == null) {
      synchronized (NacosServiceRegistry.class) {
        if (instance == null) {
          instance = new NacosServiceRegistry();
        }
      }
    }
    return instance;
  }

  @Override
  public void register(String serviceName, InetSocketAddress address) {
    try {
      namingService.registerInstance(serviceName, address.getHostName(), address.getPort());
    } catch (Exception e) {
      log.error("occur exception:", e);
      throw new RPCException(e.getMessage(), e);
    }
  }

  @Override
  public InetSocketAddress lookupService(String serviceName) {
    try {
      Instance instance = namingService.selectOneHealthyInstance(serviceName);
      return new InetSocketAddress(instance.getIp(), instance.getPort());
    } catch (Exception e) {
      log.error("occur exception:", e);
      throw new RPCException(e.getMessage(), e);
    }
  }
}
