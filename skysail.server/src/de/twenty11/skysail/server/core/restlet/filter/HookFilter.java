package de.twenty11.skysail.server.core.restlet.filter;

import de.twenty11.skysail.server.core.restlet.SkysailServerResource;


public interface HookFilter<R extends SkysailServerResource<T>,T> {

    AbstractResourceFilter<R,T> getFilter();

}
