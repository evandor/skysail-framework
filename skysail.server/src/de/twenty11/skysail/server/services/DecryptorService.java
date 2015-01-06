package de.twenty11.skysail.server.services;

import aQute.bnd.annotation.ProviderType;

@ProviderType
public interface DecryptorService {

    String decryptText(String text, String password);

}
