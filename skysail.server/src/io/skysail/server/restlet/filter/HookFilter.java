package io.skysail.server.restlet.filter;

import io.skysail.api.domain.Identifiable;
import io.skysail.server.restlet.resources.SkysailServerResource;


public interface HookFilter<R extends SkysailServerResource<T>,T extends Identifiable> {

    AbstractResourceFilter<R,T> getFilter();

}
