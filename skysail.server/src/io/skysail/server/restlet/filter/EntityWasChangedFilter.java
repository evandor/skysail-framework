package io.skysail.server.restlet.filter;

import io.skysail.server.restlet.resources.SkysailServerResource;

import java.util.List;

import org.apache.shiro.SecurityUtils;
import org.restlet.Response;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import de.twenty11.skysail.api.domain.Identifiable;
import de.twenty11.skysail.api.hooks.EntityChangedHookService;
import de.twenty11.skysail.server.app.SkysailApplication;
import de.twenty11.skysail.server.core.osgi.EventHelper;
import de.twenty11.skysail.server.core.restlet.ResponseWrapper;

public class EntityWasChangedFilter<R extends SkysailServerResource<T>, T> extends AbstractResourceFilter<R, T> {

    private static Logger logger = LoggerFactory.getLogger(EntityWasChangedFilter.class);

    private SkysailApplication application;

    public EntityWasChangedFilter(SkysailApplication skysailApplication) {
        this.application = skysailApplication;
    }

    @Override
    public FilterResult doHandle(R resource, ResponseWrapper<T> responseWrapper) {
        logger.debug("entering {}#doHandle", this.getClass().getSimpleName());
        T entity = responseWrapper.getEntity();

        String principal = (String) SecurityUtils.getSubject().getPrincipal();

        List<EntityChangedHookService> services = application.getEntityChangedHookServices();
        Response response = responseWrapper.getResponse();
        if (services != null && entity instanceof Identifiable) {
            for (EntityChangedHookService service : services) {
                service.pushEntityWasChanged(response.getRequest(), (Identifiable) entity, principal);
            }
        }

        new EventHelper(application.getEventAdmin())//
                .channel(EventHelper.GUI_MSG)//
                .info(resource.getClass().getSimpleName() + ".changed.success")//
                .fire();

        super.doHandle(resource, responseWrapper);
        return FilterResult.CONTINUE;
    }
}
