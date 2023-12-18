package com.dongdong.rpc.common.io;

import com.dongdong.rpc.common.enums.ResponseCode;
import lombok.Getter;
import lombok.ToString;

import java.io.Serializable;

@Getter
@ToString
public class RPCResponse<T> implements Serializable {
  private final T data;

  private final String code;

  private final String message;

  public RPCResponse(T data, String code, String message) {
    this.data = data;
    this.code = code;
    this.message = message;
  }

  private RPCResponse(ResponseCode rs, T data) {
    this.data = data;
    this.code = rs.getCode();
    this.message = rs.getMessage();
  }

  public static <T> RPCResponse<T> ok(T data) {
    return new RPCResponse<>(ResponseCode.OK, data);
  }

    public static <T> RPCResponse<T> fail(T data) {
        return new RPCResponse<>(ResponseCode.Fail, data);
    }

}
