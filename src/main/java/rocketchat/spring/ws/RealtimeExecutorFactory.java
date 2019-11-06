package rocketchat.spring.ws;

import java.util.concurrent.ExecutorService;

/**
 * Factory for creating executor service that will handle the incoming message processing logic. Usually new executor
 * service will be created for each successful connection event
 */
public interface RealtimeExecutorFactory {

  ExecutorService create();
}
