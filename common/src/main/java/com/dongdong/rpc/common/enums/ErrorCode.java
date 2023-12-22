package com.dongdong.rpc.common.enums;

import lombok.Getter;

@Getter
public enum ErrorCode {

  UNKNOWN_ERROR("Unknown error"),
  SERVICE_INVOCATION_FAILURE("Fail to invoke service"),
  SERVICE_CAN_NOT_BE_FOUND("Service not found"),
  SERVICE_NOT_IMPLEMENT_ANY_INTERFACE("Service does not implement any interface"),
  REQUEST_NOT_MATCH_RESPONSE("Response does not match request"),
  REGISTER_SERVICE_FAILED("Fail to register service"),
  SERIALIZER_NOT_FOUND("Serializer not found"),
  UNKNOWN_PROTOCOL("Unknown protocol"),
  UNKNOWN_PACKAGE_TYPE("Unknown package type"),
  UNKNOWN_SERIALIZER("Unknown serializer");

  private String message;

  ErrorCode(String message) {
    this.message = message;
  }
}
