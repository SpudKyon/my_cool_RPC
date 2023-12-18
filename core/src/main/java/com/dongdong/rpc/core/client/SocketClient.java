package com.dongdong.rpc.core.client;

import com.dongdong.rpc.common.cs.RPCClient;
import com.dongdong.rpc.common.io.RPCRequest;
import com.dongdong.rpc.common.io.RPCResponse;
import lombok.extern.slf4j.Slf4j;

import java.io.InputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;

@Slf4j
public class SocketClient implements RPCClient {

  private final String host;
  private final int port;

  public SocketClient(String host, int port) {
    this.host = host;
    this.port = port;
  }

  @Override
  public RPCResponse sendRequest(RPCRequest request) {
    try (Socket socket = new Socket(host, port);
         ObjectOutputStream objectOutputStream = new ObjectOutputStream(socket.getOutputStream());
         ObjectInputStream objectInputStream = new ObjectInputStream(socket.getInputStream())) {
        objectOutputStream.writeObject(request);
        objectOutputStream.flush();
        return (RPCResponse) objectInputStream.readObject();
    } catch (Exception e) {
      log.error("occur exception:", e);
      return null;
    }
  }
}
