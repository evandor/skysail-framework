package io.skysail.server.restlet.filter;

import org.restlet.Response;

import de.twenty11.skysail.server.core.restlet.Wrapper;
import io.skysail.domain.Identifiable;
import io.skysail.server.app.SkysailApplication;
import io.skysail.server.restlet.resources.*;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class PersistEntityFilter<R extends SkysailServerResource<?>, T extends Identifiable> extends
        AbstractResourceFilter<R, T> {

    public PersistEntityFilter(SkysailApplication skysailApplication) {
        // eventHelper = new EventHelper(skysailApplication.getEventAdmin());
    }
   
    @Override
    public FilterResult doHandle(R resource, Wrapper<T> responseWrapper) {
        log.debug("entering {}#doHandle", this.getClass().getSimpleName());
        Response response = responseWrapper.getResponse();
        Object entity = responseWrapper.getEntity();
        ((PostEntityServerResource<T>) resource).addEntity((T)entity);
        String id = ((T)entity).getId();
        if (id != null) {
            response.setLocationRef(response.getRequest().getResourceRef().addSegment(id.replace("#", "")));
        }
        super.doHandle(resource, responseWrapper);
        return FilterResult.CONTINUE;
    }
}
