package com.dongdong.rpc.core.serializer;

import com.dongdong.rpc.common.io.RPCRequest;
import com.dongdong.rpc.common.io.RPCResponse;
import com.dongdong.rpc.core.utils.CoolSerializer;
import com.esotericsoftware.kryo.Kryo;
import com.esotericsoftware.kryo.io.Input;
import com.esotericsoftware.kryo.io.Output;
import lombok.extern.slf4j.Slf4j;

import java.io.ByteArrayOutputStream;

@Slf4j
public class KryoSerializer implements CoolSerializer {

  static final int CODE = 2;
  private static final ThreadLocal<Kryo> kryoThreadLocal = ThreadLocal.withInitial(() -> {
    Kryo kryo = new Kryo();
    kryo.register(RPCResponse.class);
    kryo.register(RPCRequest.class);
    kryo.setReferences(true);
    kryo.setRegistrationRequired(false);
    return kryo;
  });

  @Override
  public KryoSerializer getInstance() {
    return new KryoSerializer();
  }

  @Override
  public byte[] serialize(Object obj) {
    try (Output output = new Output(new ByteArrayOutputStream())) {
      Kryo kryo = kryoThreadLocal.get();
      kryo.writeObject(output, obj);
      kryoThreadLocal.remove();
      return output.toBytes();
    } catch (Exception e) {
      log.error("error when serializing object: " + obj);
      log.error(e.getMessage());
      throw new RuntimeException("failed to serialize", e);
    }
  }

  @Override
  public Object deserialize(byte[] bytes, Class<?> clazz) {
    try {
      Kryo kryo = kryoThreadLocal.get();
      Object obj = kryo.readObject(new Input(bytes), clazz);
      kryoThreadLocal.remove();
      return obj;
    } catch (Exception e) {
      log.error("error when deserializing bytes: " + bytes);
      log.error(e.getMessage());
      throw new RuntimeException("failed to deserialize", e);
    }
  }

  @Override
  public int getCode() {
    return CODE;
  }
}
