package com.dongdong.rpc.common.io;

import lombok.*;

import java.io.Serializable;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Builder
@ToString
public class RPCRequest implements Serializable {
  /**
   * RPCRequest ID (unique)
   */
  private String requestId;
  /**
   * Interface name of the requested service
   */
  private String interfaceName;
  /**
   * Method name of the requested service
   */
  private String methodName;
  /**
   * Parameters of the requested service
   */
  private Object[] parameters;
  /**
   * Parameter types of the requested service
   */
  private Class<?>[] paramTypes;
}
