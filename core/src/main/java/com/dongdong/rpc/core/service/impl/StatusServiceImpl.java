package com.dongdong.rpc.core.service.impl;

import com.dongdong.rpc.common.service.StatusService;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class StatusServiceImpl implements StatusService {

  private String status;

  public StatusServiceImpl() {
  }

  @Override
  public String getStatus() {
    log.info("getStatus is called");
    return status != null ? status : "unknown";
  }

  @Override
  public boolean setStatus(String status) {
    log.info("setStatus is called, status: {}", status);
    this.status = status;
    return true;
  }
}
