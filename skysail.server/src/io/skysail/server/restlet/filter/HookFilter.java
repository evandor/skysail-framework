package io.skysail.server.restlet.filter;

import io.skysail.server.restlet.resources.SkysailServerResource;


public interface HookFilter<R extends SkysailServerResource<T>,T> {

    AbstractResourceFilter<R,T> getFilter();

}
