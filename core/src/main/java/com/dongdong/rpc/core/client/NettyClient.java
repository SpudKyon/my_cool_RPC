package com.dongdong.rpc.core.client;

import com.dongdong.rpc.common.cs.RPCClient;
import com.dongdong.rpc.common.exception.RPCException;
import com.dongdong.rpc.common.io.RPCRequest;
import com.dongdong.rpc.common.io.RPCResponse;
import com.dongdong.rpc.core.NacosServiceRegistry;
import com.dongdong.rpc.core.codec.CoolDecoder;
import com.dongdong.rpc.core.codec.CoolEncoder;
import com.dongdong.rpc.core.serializer.JsonSerializer;
import io.netty.bootstrap.Bootstrap;
import io.netty.channel.*;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioSocketChannel;
import io.netty.handler.logging.LogLevel;
import io.netty.handler.logging.LoggingHandler;
import io.netty.util.AttributeKey;
import lombok.extern.slf4j.Slf4j;

import java.net.InetSocketAddress;

@Slf4j
public class NettyClient<D extends ChannelInboundHandlerAdapter, E extends ChannelOutboundHandlerAdapter> implements RPCClient {

  private final Bootstrap bootstrap;

  private final String nacosHost;
  private final int nacosPort;

  private final E enc;
  private final D dec;

  public NettyClient(String host, int port, D dec, E enc) {
    this.nacosHost = host;
    this.nacosPort = port;
    this.dec = dec;
    this.enc = enc;
    bootstrap = new Bootstrap();
    try {
      NioEventLoopGroup group = new NioEventLoopGroup();
      Bootstrap handler = bootstrap.group(group)
              .channel(NioSocketChannel.class)
              .handler(new LoggingHandler(LogLevel.DEBUG))
              .option(ChannelOption.SO_KEEPALIVE, true)
              .handler(new ChannelInitializer<SocketChannel>() {
                @Override
                protected void initChannel(SocketChannel ch) throws Exception {
                  ChannelPipeline pipeline = ch.pipeline();
                  pipeline.addLast(new CoolDecoder())
                          .addLast(new CoolEncoder<>(new JsonSerializer()))
                          .addLast(new NettyClientWorker());
                }
              });
    } catch (Exception e) {
      log.error("occur exception:", e);
      this.shutdown();
    }
  }

  @Override
  public RPCResponse sendRequest(RPCRequest request) {
    try {
      NacosServiceRegistry nacos = NacosServiceRegistry.getInstance();
      InetSocketAddress address = nacos.lookupService(request.getInterfaceName());
      if (address == null) {
        throw new RPCException("no service available");
      }
      ChannelFuture future = bootstrap.connect(address.getAddress(), address.getPort()).sync();
      log.info("client connect to server {}:{}", address.getAddress(), address.getPort());
      Channel channel = future.channel();
      if (channel != null) {
        channel.writeAndFlush(request).addListener(future1 -> {
          if (future1.isSuccess()) {
            log.info("client send message: {}", request);
          } else {
            log.error("client send message failed: ", future1.cause());
            throw new RPCException("client send message failed");
          }
        });
        channel.closeFuture().sync();
        AttributeKey<RPCResponse> key = AttributeKey.valueOf("RPCResponse");
        RPCResponse rpcResponse = channel.attr(key).get();
        return rpcResponse;
      }
    } catch (Exception e) {
      log.error("occur exception: {}", e.getMessage());
    }
    return null;
  }

  public void shutdown() {
    log.info("shutting down client");
    bootstrap.config().group().shutdownGracefully();
  }
}
