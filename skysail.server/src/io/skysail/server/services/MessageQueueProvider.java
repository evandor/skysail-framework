package io.skysail.server.services;

import org.osgi.annotation.versioning.ProviderType;

@ProviderType
public interface MessageQueueProvider {

    MessageQueueHandler getMessageQueueHandler();
    
}
