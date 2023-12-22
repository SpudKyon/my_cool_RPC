package com.dongdong.rpc.common.exception;

import com.dongdong.rpc.common.enums.ErrorCode;

public class RPCException extends RuntimeException{

  private String message;

    public RPCException(String message) {
        super(message);
    }

    public RPCException(ErrorCode errorCode) {
        super(errorCode.getMessage());
    }

    public RPCException(String message, Throwable cause) {
        super(message, cause);
    }

    public RPCException(Throwable cause) {
        super(cause);
    }

    public RPCException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }
}
