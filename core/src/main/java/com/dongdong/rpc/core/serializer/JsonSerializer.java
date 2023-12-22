package com.dongdong.rpc.core.serializer;

import com.dongdong.rpc.common.io.RPCRequest;
import com.dongdong.rpc.core.utils.CoolSerializer;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;

import java.io.IOException;

@Slf4j
public class JsonSerializer implements CoolSerializer {
  static final int CODE = 1;

  private final ObjectMapper objectMapper = new ObjectMapper();

  @Override
  public JsonSerializer getInstance() {
    return new JsonSerializer();
  }

  @Override
  public byte[] serialize(Object obj) {
    try {
      return objectMapper.writeValueAsBytes(obj);
    } catch (JsonProcessingException e) {
      log.error("error when serializing object: " + obj);
      log.error(e.getMessage());
      return null;
    }
  }

  @Override
  public Object deserialize(byte[] bytes, Class<?> clazz) {
    try {
      Object obj = objectMapper.readValue(bytes, clazz);
      if (obj instanceof RPCRequest) {
        obj = handleRequest(obj);
      }
      return obj;
    } catch (IOException e) {
      log.error("error when deserializing bytes: " + bytes);
      log.error(e.getMessage());
      return null;
    }
  }

  @Override
  public int getCode() {
    return CODE;
  }

  private Object handleRequest(Object obj) throws IOException {
    RPCRequest rpcRequest = (RPCRequest) obj;
    for (int i = 0; i < rpcRequest.getParamTypes().length; i++) {
      Class<?> clazz = rpcRequest.getParamTypes()[i];
      if (!clazz.isAssignableFrom(rpcRequest.getParameters()[i].getClass())) {
        byte[] bytes = objectMapper.writeValueAsBytes(rpcRequest.getParameters()[i]);
        rpcRequest.getParameters()[i] = objectMapper.readValue(bytes, clazz);
      }
    }
    return rpcRequest;
  }
}
