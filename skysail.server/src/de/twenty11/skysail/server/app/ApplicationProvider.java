package de.twenty11.skysail.server.app;

import io.skysail.server.restlet.resources.SkysailServerResource;

import java.util.List;

import aQute.bnd.annotation.ConsumerType;

@ConsumerType
public interface ApplicationProvider extends Comparable<ApplicationProvider> {

    SkysailApplication getApplication();
    
    <T extends SkysailServerResource<?>> List<String> getTemplatePaths(Class<T> cls);

}
