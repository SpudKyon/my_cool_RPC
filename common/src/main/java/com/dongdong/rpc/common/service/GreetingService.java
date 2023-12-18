package com.dongdong.rpc.common.service;

import com.dongdong.rpc.common.RequestParam;

public interface GreetingService {
  String sayHello(int num);

  String sayHello(RequestParam param);
}
