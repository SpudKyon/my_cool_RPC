package com.dongdong.rpc.core.service.impl;

import com.dongdong.rpc.common.RequestParam;
import com.dongdong.rpc.common.service.GreetingService;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class GreetingServiceImpl implements GreetingService {

  @Override
  public String sayHello(int num) {
    log.info("sayHello is called, num: {}", num);
    return "hello there, " + (num + 10);
  }

  @Override
  public String sayHello(RequestParam param) {
    log.info("sayHello is called, param: {}", param);
    return "hello there, " + param.getData();
  }
}
