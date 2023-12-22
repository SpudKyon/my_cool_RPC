package com.dongdong.rpc.core.utils;

import com.dongdong.rpc.core.serializer.JsonSerializer;

public interface CoolSerializer {

  CoolSerializer getInstance();

  byte[] serialize(Object obj);

  Object deserialize(byte[] bytes, Class<?> clazz);

  int getCode();

  static CoolSerializer getByCode(int code) {
    switch (code) {
      case 1:
        return new JsonSerializer();
      default:
        return null;
    }
  }
}
