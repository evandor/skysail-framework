package io.skysail.server.restlet.filter;

import de.twenty11.skysail.server.core.restlet.Wrapper;
import io.skysail.domain.Identifiable;
import io.skysail.server.app.SkysailApplication;
import io.skysail.server.restlet.resources.SkysailServerResource;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class EntityWasChangedFilter<R extends SkysailServerResource<T>, T extends Identifiable> extends AbstractResourceFilter<R, T> {

    private SkysailApplication application;

    public EntityWasChangedFilter(SkysailApplication skysailApplication) {
        this.application = skysailApplication;
    }

    @Override
    public FilterResult doHandle(R resource, Wrapper<T> responseWrapper) {
        log.debug("entering {}#doHandle", this.getClass().getSimpleName());
        String infoMessage = resource.getClass().getSimpleName() + ".changed.success";
        responseWrapper.addInfo(infoMessage);

//        new EventHelper(application.getEventAdmin())//
//                .channel(EventHelper.GUI_MSG)//
//                .info(infoMessage)//
//                .fire();

        super.doHandle(resource, responseWrapper);
        return FilterResult.CONTINUE;
    }
}
