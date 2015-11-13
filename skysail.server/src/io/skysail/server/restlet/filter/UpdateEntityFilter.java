package io.skysail.server.restlet.filter;

import io.skysail.api.domain.Identifiable;
import io.skysail.api.responses.SkysailResponse;
import io.skysail.server.restlet.resources.PutEntityServerResource;

import org.slf4j.*;

import de.twenty11.skysail.server.core.restlet.Wrapper;

public class UpdateEntityFilter<R extends PutEntityServerResource<T>, T extends Identifiable> extends AbstractResourceFilter<R, T> {

    private static Logger logger = LoggerFactory.getLogger(UpdateEntityFilter.class);

    @Override
    public FilterResult doHandle(R resource, Wrapper responseWrapper) {
        logger.debug("entering {}#doHandle", this.getClass().getSimpleName());
        Object entity = responseWrapper.getEntity();
        if (entity != null) {
            SkysailResponse<T> response = resource.updateEntity((T)entity);
            responseWrapper.setEntity((T)(response.getEntity()));
            resource.setCurrentEntity(response.getEntity());
        }
        super.doHandle(resource, responseWrapper);
        return FilterResult.CONTINUE;
    }

}
