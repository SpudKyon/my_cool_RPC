package com.dongdong.rpc.core.utils;

import io.netty.channel.ChannelHandler;
import io.netty.channel.ChannelInboundHandlerAdapter;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelOutboundHandlerAdapter;
import io.netty.channel.socket.SocketChannel;
import io.netty.handler.codec.MessageToMessageDecoder;
import io.netty.handler.codec.MessageToMessageEncoder;

/**
 * CoolInitializer
 *
 * @param <D> decoder
 * @param <E> encoder
 */

public class CoolInitializer<D extends ChannelInboundHandlerAdapter, E extends ChannelOutboundHandlerAdapter> extends ChannelInitializer<SocketChannel> {

  private final E e;
  private final D d;
  private final ChannelHandler worker;

  public CoolInitializer(D d, E e, ChannelHandler worker) {
    this.d = d;
    this.e = e;
    this.worker = worker;
  }

  @Override
  protected void initChannel(SocketChannel sc) throws Exception {
    sc.pipeline()
            .addLast(d)
            .addLast(e.getClass().newInstance())
            .addLast(worker.getClass().newInstance());
  }
}
