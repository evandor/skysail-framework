package de.twenty11.skysail.server.app;

import org.restlet.resource.Resource;

public interface TranslationProvider {

    String translate(String message, String defaultMsg, Resource resource, boolean applyMarkdown, Object... subsitutions) ;
    
}
