package io.skysail.server.restlet.filter;

import io.skysail.api.responses.SkysailResponse;
import io.skysail.server.restlet.resources.PutEntityServerResource;

import org.slf4j.*;

import de.twenty11.skysail.server.core.restlet.ResponseWrapper;

public class UpdateEntityFilter<R extends PutEntityServerResource<T>, T> extends AbstractResourceFilter<R, T> {

    private static Logger logger = LoggerFactory.getLogger(UpdateEntityFilter.class);

    @Override
    public FilterResult doHandle(R resource, ResponseWrapper<T> responseWrapper) {
        logger.debug("entering {}#doHandle", this.getClass().getSimpleName());
        T entity = responseWrapper.getEntity();
        if (entity != null) {
            SkysailResponse<T> response = resource.updateEntity(entity);
            responseWrapper.setEntity(response.getEntity());
        }
        super.doHandle(resource, responseWrapper);
        return FilterResult.CONTINUE;
    }

}
