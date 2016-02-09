package io.skysail.server.services;

import java.util.List;

import org.osgi.annotation.versioning.ProviderType;

@ProviderType
public interface MessageQueueProvider {

    List<MessageQueueHandler> getMessageQueueHandler();
    
}
