package de.twenty11.skysail.server.services;

import aQute.bnd.annotation.ProviderType;

@ProviderType
public interface MailerService {
    
    void send(String to, String subject, String body);

}
