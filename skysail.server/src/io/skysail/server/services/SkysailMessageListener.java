package io.skysail.server.services;

public interface SkysailMessageListener {

    void processBody(String text);

}
