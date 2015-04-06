package io.skysail.server.restlet.filter;

import io.skysail.server.restlet.resources.SkysailServerResource;

import java.util.List;

import lombok.extern.slf4j.Slf4j;

import org.apache.shiro.SecurityUtils;
import org.restlet.Response;

import de.twenty11.skysail.api.domain.Identifiable;
import de.twenty11.skysail.api.hooks.EntityChangedHookService;
import de.twenty11.skysail.server.app.SkysailApplication;
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
        T entity = responseWrapper.getEntity();

        String principal = (String) SecurityUtils.getSubject().getPrincipal();

        List<EntityChangedHookService> services = application.getEntityChangedHookServices();
        if (services != null && entity instanceof Identifiable) {
            Response response = responseWrapper.getResponse();
            for (EntityChangedHookService service : services) {
                service.pushEntityWasAdded(response.getRequest(), (Identifiable) entity, principal);
            }
        }

        new EventHelper(application.getEventAdmin())//
                .channel(EventHelper.GUI_MSG)//
                .info(resource.getClass().getSimpleName() + ".saved.success")//
                .fire();

        super.doHandle(resource, responseWrapper);
        return FilterResult.CONTINUE;
    }
}
