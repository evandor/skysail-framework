package io.skysail.server.restlet.filter;

import io.skysail.server.app.SkysailApplication;
import io.skysail.server.restlet.resources.SkysailServerResource;
import lombok.extern.slf4j.Slf4j;
import de.twenty11.skysail.server.core.osgi.EventHelper;
import de.twenty11.skysail.server.core.restlet.ResponseWrapper;

@Slf4j
public class EntityWasAddedFilter<R extends SkysailServerResource<T>, T> extends AbstractResourceFilter<R, T> {

    private SkysailApplication application;

    public EntityWasAddedFilter(SkysailApplication skysailApplication) {
        this.application = skysailApplication;
    }

    @Override
    public FilterResult doHandle(R resource, ResponseWrapper<T> responseWrapper) {
        log.debug("entering {}#doHandle", this.getClass().getSimpleName());

        if (application.getEventAdmin() != null) {
            new EventHelper(application.getEventAdmin().get())//
                    .channel(EventHelper.GUI_MSG)//
                    .info(resource.getClass().getSimpleName() + ".saved.success")//
                    .fire();
        }
        super.doHandle(resource, responseWrapper);
        return FilterResult.CONTINUE;
    }
}
