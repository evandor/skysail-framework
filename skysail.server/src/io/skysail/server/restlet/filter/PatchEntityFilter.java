package io.skysail.server.restlet.filter;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import de.twenty11.skysail.server.core.restlet.Wrapper;
import io.skysail.api.responses.SkysailResponse;
import io.skysail.domain.Identifiable;
import io.skysail.server.restlet.resources.PatchEntityServerResource;

public class PatchEntityFilter<R extends PatchEntityServerResource<T>, T extends Identifiable> extends AbstractResourceFilter<R, T> {

    private static Logger logger = LoggerFactory.getLogger(PatchEntityFilter.class);

    @Override
    public FilterResult doHandle(R resource, Wrapper<T> responseWrapper) {
        logger.debug("entering {}#doHandle", this.getClass().getSimpleName());
        Object entity = responseWrapper.getEntity();
        if (entity != null) {
            resource.updateEntity((T)entity);
            SkysailResponse<T> response = new SkysailResponse<>();
            //responseWrapper.setEntity((T)(response.getEntity()));
            //resource.setCurrentEntity(response.getEntity());
        }
        super.doHandle(resource, responseWrapper);
        return FilterResult.CONTINUE;
    }

}
