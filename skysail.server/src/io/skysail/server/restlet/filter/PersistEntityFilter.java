package io.skysail.server.restlet.filter;

import io.skysail.api.domain.Identifiable;
import io.skysail.server.app.SkysailApplication;
import io.skysail.server.restlet.resources.*;

import org.restlet.Response;
import org.slf4j.*;

import de.twenty11.skysail.server.core.restlet.ResponseWrapper;

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
        ((PostEntityServerResource<T>) resource).addEntity(entity);
        // TODO filter of its own?
        if (entity instanceof Identifiable) {
            String id = ((Identifiable) entity).getId();
            if (id != null) {
                response.setLocationRef(response.getRequest().getResourceRef().addSegment(id.replace("#","")));
            }
        }
        super.doHandle(resource, responseWrapper);
        return FilterResult.CONTINUE;
    }
}
