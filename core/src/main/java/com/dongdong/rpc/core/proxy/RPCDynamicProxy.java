package com.dongdong.rpc.core.proxy;

import com.dongdong.rpc.common.cs.RPCClient;
import com.dongdong.rpc.common.exception.RPCException;
import com.dongdong.rpc.common.io.RPCRequest;
import com.dongdong.rpc.common.io.RPCResponse;
import lombok.extern.slf4j.Slf4j;
import net.sf.cglib.proxy.Enhancer;
import net.sf.cglib.proxy.MethodInterceptor;
import net.sf.cglib.proxy.MethodProxy;

import java.lang.reflect.Method;

@Slf4j
public class RPCDynamicProxy implements MethodInterceptor {

  private final RPCClient client;

  public RPCDynamicProxy(RPCClient client) {
    this.client = client;
  }

  @SuppressWarnings("unchecked")
  public <T> T getProxy(Class<T> interfaceClass) {
    Enhancer enhancer = new Enhancer();
    enhancer.setSuperclass(interfaceClass);
    enhancer.setCallback(this);
    return (T) enhancer.create();
  }


  @Override
  public Object intercept(Object obj, Method method, Object[] args, MethodProxy proxy) throws Throwable {
    if (client == null) {
      throw new RuntimeException("client is null");
    }
    try {
      RPCRequest request = RPCRequest.builder()
              .interfaceName(method.getDeclaringClass().getName())  // get interface name
              .methodName(method.getName())                         // get method name
              .parameters(args)                                     // get parameters
              .paramTypes(method.getParameterTypes())               // get parameter types
              .build();
      RPCResponse response = client.sendRequest(request);
      if (response == null) {
        log.error("response is null for request: {}", request);
        throw new RPCException("response is null");
      }
      log.debug("request: {}", request);
      log.debug("response: {}", response);
      if (response.getCode() == 200) {
        return response.getData();
      } else {
        throw new RPCException(response.getMessage());
      }
    } catch (Exception e) {
      log.error("error: {}", e.getMessage());
      client.shutdown();
    }
    return null;
  }
}
