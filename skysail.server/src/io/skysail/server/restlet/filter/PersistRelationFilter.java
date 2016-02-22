package io.skysail.server.restlet.filter;

import org.restlet.Response;

import de.twenty11.skysail.server.core.restlet.Wrapper;
import io.skysail.domain.Identifiable;
import io.skysail.server.app.SkysailApplication;
import io.skysail.server.restlet.resources.PostRelationResource;
import io.skysail.server.restlet.resources.SkysailServerResource;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class PersistRelationFilter<R extends SkysailServerResource<?>, T extends Identifiable> extends
        AbstractResourceFilter<R, T> {

    public PersistRelationFilter(SkysailApplication skysailApplication) {
        // eventHelper = new EventHelper(skysailApplication.getEventAdmin());
    }
   
    @Override
    public FilterResult doHandle(R resource, Wrapper<T> responseWrapper) {
        log.debug("entering {}#doHandle", this.getClass().getSimpleName());
        Response response = responseWrapper.getResponse();
        Object entity = responseWrapper.getEntity();
        ((PostRelationResource) resource).addRelations(entity);
//        String id = ((T)entity).getId();
//        if (id != null) {
//            response.setLocationRef(response.getRequest().getResourceRef().addSegment(id.replace("#", "")));
//        }
        super.doHandle(resource, responseWrapper);
        return FilterResult.CONTINUE;
    }
}
