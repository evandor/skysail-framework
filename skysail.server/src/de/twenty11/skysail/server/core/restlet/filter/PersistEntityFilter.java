package de.twenty11.skysail.server.core.restlet.filter;

import io.skysail.api.responses.SkysailResponse;

import org.restlet.Response;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import de.twenty11.skysail.api.domain.Identifiable;
import de.twenty11.skysail.server.app.SkysailApplication;
import de.twenty11.skysail.server.core.restlet.PostEntityServerResource;
import de.twenty11.skysail.server.core.restlet.ResponseWrapper;
import de.twenty11.skysail.server.core.restlet.SkysailServerResource;

public class PersistEntityFilter<R extends SkysailServerResource<T>, T> extends AbstractResourceFilter<R, T> {

    private static Logger logger = LoggerFactory.getLogger(PersistEntityFilter.class);

    public PersistEntityFilter(SkysailApplication skysailApplication) {
        // eventHelper = new EventHelper(skysailApplication.getEventAdmin());
    }

    @Override
    public FilterResult doHandle(R resource, ResponseWrapper<T> responseWrapper) {
        logger.debug("entering {}#doHandle", this.getClass().getSimpleName());
        Response response = responseWrapper.getResponse();
        T entity = responseWrapper.getEntity();
        SkysailResponse<T> result = (SkysailResponse<T>) ((PostEntityServerResource<T>) resource).addEntity(entity);
        // TODO filter of its own?
        if (entity instanceof Identifiable) {
            String id = ((Identifiable) entity).getId();
            response.setLocationRef(response.getRequest().getResourceRef().addSegment(id));
        }
        super.doHandle(resource, responseWrapper);
        return FilterResult.CONTINUE;
    }
}
