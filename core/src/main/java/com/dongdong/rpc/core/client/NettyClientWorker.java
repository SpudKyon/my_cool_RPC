package com.dongdong.rpc.core.client;

import com.dongdong.rpc.common.io.RPCResponse;
import io.netty.channel.*;
import io.netty.util.AttributeKey;
import io.netty.util.ReferenceCountUtil;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@ChannelHandler.Sharable
public class NettyClientWorker extends SimpleChannelInboundHandler<RPCResponse> {


  @Override
  protected void channelRead0(ChannelHandlerContext ctx, RPCResponse msg) throws Exception {
    try {
      log.debug("new connection: {}, msg: {}", ctx.channel().remoteAddress(), msg);
      AttributeKey<RPCResponse> key = AttributeKey.valueOf("RPCResponse");
      ctx.channel().attr(key).set(msg);
      ctx.channel().close();
    } finally {
      ReferenceCountUtil.release(msg);
    }
  }

  @Override
  public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
    log.error("error when handling request: " + cause.getMessage());
    ctx.close();
  }
}
