package com.dongdong.rpc.common.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public enum IOType {

  REQUEST_PACK(0),
  RESPONSE_PACK(1);

  private final int code;

}