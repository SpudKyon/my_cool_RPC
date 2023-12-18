package com.dongdong.rpc.core.service.impl;

import com.dongdong.rpc.common.RequestParam;
import com.dongdong.rpc.common.service.GreetingService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class GreetingServiceImpl implements GreetingService {

  private static final Logger LOGGER = LoggerFactory.getLogger(GreetingServiceImpl.class);

  @Override
  public String sayHello(int num) {
    LOGGER.info("sayHello is called, num: {}", num);
    return "hello there, " + (num + 10);
  }

  @Override
  public String sayHello(RequestParam param) {
    LOGGER.info("sayHello is called, param: {}", param);
    return "hello there, " + param.getData();
  }
}
