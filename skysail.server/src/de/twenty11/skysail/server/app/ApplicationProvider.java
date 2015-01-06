package de.twenty11.skysail.server.app;

import java.util.List;

import aQute.bnd.annotation.ConsumerType;
import de.twenty11.skysail.server.core.restlet.SkysailServerResource;

@ConsumerType
public interface ApplicationProvider extends Comparable<ApplicationProvider> {

    SkysailApplication getApplication();
    
    <T extends SkysailServerResource<?>> List<String> getTemplatePaths(Class<T> cls);

}
