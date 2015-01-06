package de.twenty11.skysail.server.services;

import java.util.concurrent.Callable;

import aQute.bnd.annotation.ProviderType;

@ProviderType
public interface SkysailThreadPool {

    void start();

    void stop();

    void submit(Callable<String> task);

    String get();

}
