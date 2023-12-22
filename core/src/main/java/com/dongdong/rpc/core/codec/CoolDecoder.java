package com.dongdong.rpc.core.codec;

import com.dongdong.rpc.common.enums.ErrorCode;
import com.dongdong.rpc.common.enums.IOType;
import com.dongdong.rpc.common.exception.RPCException;
import com.dongdong.rpc.common.io.RPCRequest;
import com.dongdong.rpc.common.io.RPCResponse;
import com.dongdong.rpc.core.utils.CoolSerializer;
import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandler;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.ReplayingDecoder;
import lombok.extern.slf4j.Slf4j;

import java.util.List;

@Slf4j
public class CoolDecoder extends ReplayingDecoder {
  private static final int MAGIC_NUMBER = 0xBABECAFE;

  @Override
  protected void decode(ChannelHandlerContext ctx, ByteBuf in, List<Object> out) throws Exception {
    int magic = in.readInt();
    if(magic != MAGIC_NUMBER) {
      log.error("unknown protocol: {}", magic);
      throw new RPCException(ErrorCode.UNKNOWN_PROTOCOL);
    }
    int packageCode = in.readInt();
    Class<?> packageClass;
    if(packageCode == IOType.REQUEST_PACK.getCode()) {
      packageClass = RPCRequest.class;
    } else if(packageCode == IOType.RESPONSE_PACK.getCode()) {
      packageClass = RPCResponse.class;
    } else {
      log.error("unknown package type: {}", packageCode);
      throw new RPCException(ErrorCode.UNKNOWN_PACKAGE_TYPE);
    }
    int serializerCode = in.readInt();
    CoolSerializer serializer = CoolSerializer.getByCode(serializerCode);
    if(serializer == null) {
      log.error("unknown deserializer: {}", serializerCode);
      throw new RPCException(ErrorCode.UNKNOWN_SERIALIZER);
    }
    int length = in.readInt();
    byte[] bytes = new byte[length];
    in.readBytes(bytes);
    Object obj = serializer.deserialize(bytes, packageClass);
    out.add(obj);
  }
}
