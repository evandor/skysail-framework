package io.skysail.server.services;

public interface MessageQueueHandler {

    void send(String topic, String message);

}
