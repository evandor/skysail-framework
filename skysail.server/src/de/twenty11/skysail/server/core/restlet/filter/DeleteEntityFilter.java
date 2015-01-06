package de.twenty11.skysail.server.core.restlet.filter;

import org.restlet.Response;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import de.twenty11.skysail.server.core.restlet.EntityServerResource;
import de.twenty11.skysail.server.core.restlet.ResponseWrapper;

public class DeleteEntityFilter<R extends EntityServerResource<T>, T> extends AbstractResourceFilter<R, T> {

    private static Logger logger = LoggerFactory.getLogger(DeleteEntityFilter.class);

    @Override
    public FilterResult doHandle(R resource, Response response, ResponseWrapper<T> responseWrapper) {
        logger.debug("entering {}#doHandle", this.getClass().getSimpleName());
        resource.eraseEntity();
        super.doHandle(resource, response, responseWrapper);
        return FilterResult.CONTINUE;
    }

}
