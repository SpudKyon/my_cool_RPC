package com.dongdong.rpc.common.exception;

public class RPCException extends RuntimeException{

  private String message;

    public RPCException(String message) {
        super(message);
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
