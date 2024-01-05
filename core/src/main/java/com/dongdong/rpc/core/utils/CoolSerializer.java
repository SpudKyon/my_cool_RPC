package com.dongdong.rpc.core.utils;

import com.dongdong.rpc.core.serializer.HessianSerializer;
import com.dongdong.rpc.core.serializer.JsonSerializer;
import com.dongdong.rpc.core.serializer.KryoSerializer;

public interface CoolSerializer {

  CoolSerializer getInstance();

  byte[] serialize(Object obj);

  Object deserialize(byte[] bytes, Class<?> clazz);

  int getCode();

  static CoolSerializer getByCode(int code) {
    return switch (code) {
      case 1 -> new JsonSerializer();
      case 2 -> new KryoSerializer();
      case 3 -> new HessianSerializer();
      default -> null;
    };
  }
}
