package com.dongdong.rpc.core.server;

import com.dongdong.rpc.common.cs.RPCServer;
import com.dongdong.rpc.core.codec.CoolDecoder;
import com.dongdong.rpc.core.codec.CoolEncoder;
import com.dongdong.rpc.core.serializer.JsonSerializer;
import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.*;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import io.netty.handler.logging.LogLevel;
import io.netty.handler.logging.LoggingHandler;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class NettyServer<D extends ChannelInboundHandlerAdapter, E extends ChannelOutboundHandlerAdapter> implements RPCServer {

  /**
   * interface name -> service object
   */
  private final ServiceMap serviceMap;

  private final int port;
  private final E enc;
  private final D dec;

  public NettyServer(int port, D dec, E enc) {
    this.port = port;
    this.dec = dec;
    this.enc = enc;
    serviceMap = ServiceMap.getInstance();
  }

  @Override
  public void start() {
    ServerBootstrap bootstrap = new ServerBootstrap();
    try (EventLoopGroup listenGroup = new NioEventLoopGroup();
         EventLoopGroup workerGroup = new NioEventLoopGroup();) {
      bootstrap.group(listenGroup, workerGroup)
              .channel(NioServerSocketChannel.class)
              .handler(new LoggingHandler(LogLevel.DEBUG))
              .option(ChannelOption.SO_KEEPALIVE, true)
              .childOption(ChannelOption.TCP_NODELAY, true)
              .childHandler(new ChannelInitializer<SocketChannel>() {
                @Override
                protected void initChannel(SocketChannel ch) throws Exception {
                  ChannelPipeline pipeline = ch.pipeline();
                  pipeline.addLast(new CoolDecoder())
                          .addLast(new CoolEncoder<>(new JsonSerializer()))
                          .addLast(new NettyServerWorker());
                }
              });
      ChannelFuture future = bootstrap.bind(port).sync();
      future.channel().closeFuture().sync(); // block until server socket closed
    } catch (Exception e) {
      log.error("error when starting server: " + e.getMessage());
      throw new RuntimeException(e);
    }
  }

  @Override
  public void stop() {

  }
}
