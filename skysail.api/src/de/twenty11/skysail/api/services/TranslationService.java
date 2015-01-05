package de.twenty11.skysail.api.services;

import org.restlet.Request;

import aQute.bnd.annotation.ProviderType;

@ProviderType
public interface TranslationService {

    String translate(ClassLoader cl, Request request, String key, String defaultValue);

}
