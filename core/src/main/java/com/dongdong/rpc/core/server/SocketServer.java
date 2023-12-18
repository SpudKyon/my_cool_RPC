package com.dongdong.rpc.core.server;

import com.dongdong.rpc.common.cs.RPCServer;
import lombok.extern.slf4j.Slf4j;

import java.net.ServerSocket;
import java.net.Socket;
import java.util.concurrent.*;

@Slf4j
public class SocketServer implements RPCServer {

  private final ExecutorService threadPool;

  /**
   * interface name -> service object
   */
  static ConcurrentHashMap<String, Object> serviceMap = new ConcurrentHashMap<>();

  private final int port;

  public SocketServer(int port) {
    this.port = port;
    /**
     * corePoolSize: the number of threads to keep in the pool, even if they are idle, unless {@code allowCoreThreadTimeOut} is set
     */
    int corePoolSize = 5;
    /**
     * maximumPoolSize: the maximum number of threads to allow in the pool
     */
    int maximumPoolSize = 50;
    /**
     * keepAliveTime: when the number of threads is greater than the core, this is the maximum time that excess idle threads will wait for new tasks before terminating.
     */
    long keepAliveTime = 30;
    BlockingQueue<Runnable> workingQueue = new ArrayBlockingQueue<>(100);
    ThreadFactory threadFactory = Executors.defaultThreadFactory();
    threadPool = new ThreadPoolExecutor(corePoolSize, maximumPoolSize, keepAliveTime, TimeUnit.SECONDS, workingQueue, threadFactory);
  }

  @Override
  public boolean addService(String serviceName, Object service) {
    if (serviceMap.containsKey(serviceName)) {
      return false;
    }
    serviceMap.put(serviceName, service);
    return true;
  }

  @Override
  public void start() {
    try(ServerSocket ss = new ServerSocket(port);) {
      log.info("server started, listening on port: " + port);
      Socket socket;
      while ((socket = ss.accept()) != null) {
        log.debug("new connection: " + socket.getInetAddress() + ":" + socket.getPort());
        threadPool.execute(new SocketServerWorker(socket));
      }
    } catch (Exception e) {
      log.error("error when starting server: " + e.getMessage());
      e.printStackTrace();
    }
  }

  @Override
  public void stop() {

  }
}
