package de.twenty11.skysail.server.core.restlet.filter;

import java.util.List;

import org.apache.shiro.SecurityUtils;
import org.restlet.Response;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import de.twenty11.skysail.api.domain.Identifiable;
import de.twenty11.skysail.api.hooks.EntityChangedHookService;
import de.twenty11.skysail.server.app.SkysailApplication;
import de.twenty11.skysail.server.core.restlet.ResponseWrapper;
import de.twenty11.skysail.server.core.restlet.SkysailServerResource;

public class EntityWasAddedFilter<R extends SkysailServerResource<T>, T> extends AbstractResourceFilter<R, T> {

    private static Logger logger = LoggerFactory.getLogger(EntityWasAddedFilter.class);

    private SkysailApplication application;

    public EntityWasAddedFilter(SkysailApplication skysailApplication) {
        this.application = skysailApplication;
    }

    @Override
    public FilterResult doHandle(R resource, Response response, ResponseWrapper<T> responseWrapper) {
        logger.debug("entering {}#doHandle", this.getClass().getSimpleName());
        T entity = responseWrapper.getEntity();

        String principal = (String) SecurityUtils.getSubject().getPrincipal();

        List<EntityChangedHookService> services = application.getEntityChangedHookServices();
        if (services != null && entity instanceof Identifiable) {
            for (EntityChangedHookService service : services) {
                service.pushEntityWasAdded(response.getRequest(), (Identifiable) entity, principal);
            }
        }
        super.doHandle(resource, response, responseWrapper);
        return FilterResult.CONTINUE;
    }
}
