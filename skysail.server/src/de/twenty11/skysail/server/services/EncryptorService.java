package de.twenty11.skysail.server.services;

import org.osgi.annotation.versioning.ProviderType;

@ProviderType
public interface EncryptorService {

    String encryptText(String text, String password);

}
