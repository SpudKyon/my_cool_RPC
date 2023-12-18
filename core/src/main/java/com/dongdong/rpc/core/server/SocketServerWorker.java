package com.dongdong.rpc.core.server;

import com.dongdong.rpc.common.exception.RPCException;
import com.dongdong.rpc.common.io.RPCRequest;
import com.dongdong.rpc.common.io.RPCResponse;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.lang.reflect.Method;
import java.net.Socket;

@AllArgsConstructor
@Slf4j
public class SocketServerWorker implements Runnable {

  private final Socket socket;

  @Override
  public void run() {
    try (ObjectInputStream os = new ObjectInputStream(socket.getInputStream());
         ObjectOutputStream oos = new ObjectOutputStream(socket.getOutputStream())) {
      RPCRequest request = (RPCRequest) os.readObject();
      if (!SocketServer.serviceMap.containsKey(request.getInterfaceName())) {
        log.error("service not found: " + request.getInterfaceName());
        throw new RPCException("service not found: " + request.getInterfaceName());
      }
      Method method = Class.forName(request.getInterfaceName())
              .getMethod(request.getMethodName(), request.getParamTypes());
      Object service = SocketServer.serviceMap.get(request.getInterfaceName());
      Object result = method.invoke(service, request.getParameters());
      log.debug("result: " + result);
      oos.writeObject(RPCResponse.ok(result));
      oos.flush();
    } catch (Exception e) {
      log.error("error when handling request: " + e.getMessage());
      e.printStackTrace();
    }
  }
}
