package de.twenty11.skysail.server.services;

import org.osgi.annotation.versioning.ProviderType;

@ProviderType
public interface DecryptorService {

    String decryptText(String text, String password);

}
