package com.dongdong.rpc.common.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.ToString;

@AllArgsConstructor
@ToString
@Getter
public enum ExceptionCode {

  UNKNOWN_EXCEPTION("unknown exception"),
  CLIENT_CONNECT_SERVER_FAILURE("client connect server failure"),
  SERVICE_INVOCATION_FAILURE("service invocation failure"),
  SERVICE_CAN_NOT_BE_FOUND("service can not be found"),
  SERVICE_NOT_IMPLEMENT_ANY_INTERFACE("service not implement any interface"),
  REQUEST_NOT_MATCH_RESPONSE("request not match response");

  private final String message;

}
