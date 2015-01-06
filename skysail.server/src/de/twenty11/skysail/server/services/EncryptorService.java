package de.twenty11.skysail.server.services;

import aQute.bnd.annotation.ProviderType;

@ProviderType
public interface EncryptorService {

    String encryptText(String text, String password);

}
