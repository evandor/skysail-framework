package io.skysail.server.restlet.filter;

import de.twenty11.skysail.server.core.osgi.EventHelper;
import de.twenty11.skysail.server.core.restlet.Wrapper;
import io.skysail.api.domain.Identifiable;
import io.skysail.server.app.SkysailApplication;
import io.skysail.server.restlet.resources.SkysailServerResource;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class EntityWasAddedFilter<R extends SkysailServerResource<T>, T extends Identifiable> extends AbstractResourceFilter<R, T> {

    private SkysailApplication application;

    public EntityWasAddedFilter(SkysailApplication skysailApplication) {
        this.application = skysailApplication;
    }

    @Override
    public FilterResult doHandle(R resource, Wrapper responseWrapper) {
        log.debug("entering {}#doHandle", this.getClass().getSimpleName());

        if (application.getEventAdmin() != null) {
            new EventHelper(application.getEventAdmin())//
                    .channel(EventHelper.GUI_MSG)//
                    .info(resource.getClass().getSimpleName() + ".saved.success")//
                    .fire();
        }
        super.doHandle(resource, responseWrapper);
        return FilterResult.CONTINUE;
    }
}
