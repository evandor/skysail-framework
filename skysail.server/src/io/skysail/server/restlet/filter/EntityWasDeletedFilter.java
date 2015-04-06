package io.skysail.server.restlet.filter;

import io.skysail.server.app.SkysailApplication;
import io.skysail.server.restlet.resources.SkysailServerResource;

import java.util.List;

import org.apache.shiro.SecurityUtils;
import org.restlet.Response;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import de.twenty11.skysail.api.hooks.EntityChangedHookService;
import de.twenty11.skysail.server.core.osgi.EventHelper;
import de.twenty11.skysail.server.core.restlet.ResponseWrapper;

public class EntityWasDeletedFilter<R extends SkysailServerResource<T>, T> extends AbstractResourceFilter<R, T> {

    private static Logger logger = LoggerFactory.getLogger(EntityWasDeletedFilter.class);

    private SkysailApplication application;

    public EntityWasDeletedFilter(SkysailApplication skysailApplication) {
        this.application = skysailApplication;
    }

    @Override
    public FilterResult doHandle(R resource, ResponseWrapper<T> responseWrapper) {
        logger.debug("entering {}#doHandle", this.getClass().getSimpleName());
        T entity = responseWrapper.getEntity();

        String principal = (String) SecurityUtils.getSubject().getPrincipal();

        List<EntityChangedHookService> services = application.getEntityChangedHookServices();
        if (services != null) {
            Response response = responseWrapper.getResponse();
            for (EntityChangedHookService service : services) {
                service.pushEntityWasDeleted(response.getRequest(), principal);
            }
        }

        new EventHelper(application.getEventAdmin())//
                .channel(EventHelper.GUI_MSG)//
                .info(resource.getClass().getSimpleName() + ".deleted.success")//
                .fire();

        super.doHandle(resource, responseWrapper);
        return FilterResult.CONTINUE;
    }
}
