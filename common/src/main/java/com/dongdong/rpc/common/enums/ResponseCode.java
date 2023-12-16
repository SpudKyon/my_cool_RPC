package com.dongdong.rpc.common.enums;

import lombok.Getter;

@Getter
public enum ResponseCode {

  OK("200", "OK"),
  Fail("500", "Fail"),
  NotFound("404", "Not Found"),
  BadRequest("400", "Bad RPCRequest"),
  Unauthorized("401", "Unauthorized"),
  Forbidden("403", "Forbidden");

  private final String code;
  private final String message;

  ResponseCode(String code, String message) {
    this.code = code;
    this.message = message;
  }

}
