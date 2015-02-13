package io.skysail.api.text;

import java.util.Locale;
import java.util.Optional;

import org.restlet.Request;

import aQute.bnd.annotation.ProviderType;

@ProviderType
public interface Store {

    Optional<String> get(String key);

    Optional<String> get(String key, ClassLoader cl);

    Optional<String> get(String key, ClassLoader cl, Request request);

    Optional<String> get(String key, ClassLoader cl, Request request, Locale locale);

}
