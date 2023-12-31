package com.dongdong.rpc.common;

import lombok.*;

import java.io.Serializable;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class RequestParam<T> implements Serializable {
  private T data;
  /**
   * class of data
   */
  private Class<T> clazz;
}
