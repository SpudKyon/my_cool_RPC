package com.dongdong.rpc.core.serializer;

import com.alibaba.nacos.shaded.io.grpc.Status;
import com.caucho.hessian.io.HessianInput;
import com.caucho.hessian.io.HessianOutput;
import com.dongdong.rpc.common.exception.RPCException;
import com.dongdong.rpc.core.utils.CoolSerializer;
import lombok.extern.slf4j.Slf4j;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.Arrays;

@Slf4j
public class HessianSerializer implements CoolSerializer {

  static final int CODE = 3;

  @Override
  public CoolSerializer getInstance() {
    return new HessianSerializer();
  }

  public byte[] serialize(Object obj) {
    HessianOutput hessianOutput = null;
    try (ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream()) {
      hessianOutput = new HessianOutput(byteArrayOutputStream);
      hessianOutput.writeObject(obj);
      return byteArrayOutputStream.toByteArray();
    } catch (IOException e) {
      log.error("error when serializing object: " + obj);
      throw new RPCException("error when serializing object: " + obj + " cause: " + e.getMessage());
    } finally {
      if (hessianOutput != null) {
        try {
          hessianOutput.close();
        } catch (IOException e) {
          log.error("error when closing hessianOutput: " + e.getMessage());
        }
      }
    }
  }

  @Override
  public Object deserialize(byte[] bytes, Class<?> clazz) {
    HessianInput hessianInput = null;
    try (ByteArrayInputStream byteArrayInputStream = new ByteArrayInputStream(bytes)) {
      hessianInput = new HessianInput(byteArrayInputStream);
      return hessianInput.readObject();
    } catch (IOException e) {
      log.error("error when deserializing bytes: " + Arrays.toString(bytes));
      throw new RPCException("error when deserializing bytes: " + Arrays.toString(bytes) + " cause: " + e.getMessage());
    } finally {
      if (hessianInput != null)
        hessianInput.close();
    }
  }

  @Override
  public int getCode() {
    return CODE;
  }
}
