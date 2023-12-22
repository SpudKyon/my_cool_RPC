package com.dongdong.rpc.core.codec;

import com.dongdong.rpc.common.enums.IOType;
import com.dongdong.rpc.common.io.RPCRequest;
import com.dongdong.rpc.core.utils.CoolSerializer;
import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandler;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.MessageToByteEncoder;

public class CoolEncoder<S extends CoolSerializer> extends MessageToByteEncoder<Object> {

  private static final int MAGIC_NUMBER = 0xBABECAFE;

  private final S serializer;

  public CoolEncoder(S serializer) {
    this.serializer = serializer;
  }

  @Override
  protected void encode(ChannelHandlerContext ctx, Object msg, ByteBuf out) throws Exception {
    out.writeInt(MAGIC_NUMBER);
    if (msg instanceof RPCRequest) {
      out.writeInt(IOType.REQUEST_PACK.getCode());
    } else {
      out.writeInt(IOType.RESPONSE_PACK.getCode());
    }
    out.writeInt(serializer.getCode());
    byte[] bytes = serializer.serialize(msg);
    out.writeInt(bytes.length);
    out.writeBytes(bytes);
  }
}
