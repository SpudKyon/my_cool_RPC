package com.dongdong.rpc.core.proxy;

import com.dongdong.rpc.common.cs.RPCClient;
import com.dongdong.rpc.common.exception.RPCException;
import com.dongdong.rpc.common.io.RPCRequest;
import com.dongdong.rpc.common.io.RPCResponse;
import lombok.Getter;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;

@Getter
@Setter
@Slf4j
public class RPCStaticProxy implements InvocationHandler {

  private final RPCClient client;

  public RPCStaticProxy(RPCClient client) {
    this.client = client;
  }

  @SuppressWarnings("unchecked")
  public <T> T getProxy(Class<T> clazz) {
    return (T) Proxy.newProxyInstance(clazz.getClassLoader(), new Class<?>[]{clazz}, this);
  }

  @Override
  public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
    try {
      if (client == null) {
        throw new RPCException("client is null");
      }
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
