package com.dongdong.rpc.common.cs;

import com.dongdong.rpc.common.io.RPCRequest;
import com.dongdong.rpc.common.io.RPCResponse;

public interface RPCClient {
  RPCResponse sendRequest(RPCRequest request);

  void shutdown();
}
