package io.skysail.server.restlet.filter;

import io.skysail.domain.Identifiable;
import io.skysail.server.restlet.resources.EntityServerResource;

import org.slf4j.*;

import de.twenty11.skysail.server.core.restlet.Wrapper;

public class DeleteEntityFilter<R extends EntityServerResource<T>, T extends Identifiable> extends AbstractResourceFilter<R, T> {

    private static Logger logger = LoggerFactory.getLogger(DeleteEntityFilter.class);

    @Override
    public FilterResult doHandle(R resource, Wrapper responseWrapper) {
        logger.debug("entering {}#doHandle", this.getClass().getSimpleName());
        resource.eraseEntity();
        super.doHandle(resource, responseWrapper);
        return FilterResult.CONTINUE;
    }

}
