package com.dongdong.rpc.core.server;

import com.dongdong.rpc.common.exception.RPCException;
import com.dongdong.rpc.common.io.RPCRequest;
import com.dongdong.rpc.common.io.RPCResponse;
import io.netty.channel.*;
import io.netty.util.ReferenceCountUtil;
import lombok.extern.slf4j.Slf4j;

import java.lang.reflect.Method;

@Slf4j
@ChannelHandler.Sharable
public class NettyServerWorker extends SimpleChannelInboundHandler<RPCRequest> {

  @Override
  protected void channelRead0(ChannelHandlerContext ctx, RPCRequest msg) throws Exception {
    ServiceMap serviceMap = ServiceMap.getInstance();
    try {
      log.debug("new connection: {}, msg: {}", ctx.channel().remoteAddress(), msg);
      String serviceName = msg.getInterfaceName();
      if (!serviceMap.containsKey(serviceName)) {
        throw new RPCException("service not found: " + serviceName);
      }
      Method method = Class.forName(serviceName)
              .getMethod(msg.getMethodName(), msg.getParamTypes());
      Object service = serviceMap.get(serviceName);
      Object result = method.invoke(service, msg.getParameters());
      log.debug("result: " + result);
      ChannelFuture future = ctx.writeAndFlush(RPCResponse.ok(result));
      future.addListener(ChannelFutureListener.CLOSE); // close connection after sending response
    } catch (RPCException e) {
      log.error("error when handling request: " + e.getMessage());
      ChannelFuture future = ctx.writeAndFlush(RPCResponse.fail(e.getMessage()));
      future.addListener(ChannelFutureListener.CLOSE); // close connection after sending response
    } finally {
      ReferenceCountUtil.release(msg);
    }
  }

  @Override
  public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
    log.error("error when handling request: " + cause.getMessage());
    ChannelFuture future = ctx.writeAndFlush(RPCResponse.fail(cause.getMessage()));
    future.addListener(ChannelFutureListener.CLOSE); // close connection after sending response
    ctx.close();
  }
}