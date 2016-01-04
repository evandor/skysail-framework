package io.skysail.server.services;

import org.osgi.annotation.versioning.ProviderType;

@ProviderType
public interface MailerService {
    
    void send(String to, String subject, String body);

}
