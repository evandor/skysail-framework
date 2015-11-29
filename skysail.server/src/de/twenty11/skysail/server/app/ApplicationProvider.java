package de.twenty11.skysail.server.app;

import java.util.List;

import io.skysail.server.app.SkysailApplication;
import io.skysail.server.restlet.resources.SkysailServerResource;

@org.osgi.annotation.versioning.ConsumerType
public interface ApplicationProvider extends Comparable<ApplicationProvider> {

    SkysailApplication getApplication();
    
    <T extends SkysailServerResource<?>> List<String> getTemplatePaths(Class<T> cls);

}
