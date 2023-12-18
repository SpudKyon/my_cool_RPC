package com.dongdong.rpc.core.proxy;

import com.dongdong.rpc.common.cs.RPCClient;
import com.dongdong.rpc.common.exception.RPCException;
import com.dongdong.rpc.common.io.RPCRequest;
import com.dongdong.rpc.common.io.RPCResponse;
import com.dongdong.rpc.core.client.SocketClient;
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

  private RPCClient client;
  private final String host;
  private final int port;

  public RPCStaticProxy(String host, int port) {
    this.host = host;
    this.port = port;
    client = new SocketClient(host, port);
  }

  @SuppressWarnings("unchecked")
  public <T> T getProxy(Class<T> clazz) {
    return (T) Proxy.newProxyInstance(clazz.getClassLoader(), new Class<?>[]{clazz}, this);
  }


  @Override
  public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
    if (client == null) {
      throw new RPCException("client is null");
    }
    RPCRequest request = RPCRequest.builder()
            .interfaceName(method.getDeclaringClass().getName()) // get interface name
            .methodName(method.getName()) // get method name
            .parameters(args) // get parameters
            .paramTypes(method.getParameterTypes()) // get parameter types
            .build();
    log.info("sending request to {}:{}", host, port);
    RPCResponse response = client.sendRequest(request);
    log.debug("request: {}", request);
    log.debug("response: {}", response);
    return response.getData();
  }
}