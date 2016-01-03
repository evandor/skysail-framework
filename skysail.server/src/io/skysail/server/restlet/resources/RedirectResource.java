package io.skysail.server.restlet.resources;

import java.util.Set;

import org.restlet.representation.Variant;
import org.restlet.resource.Get;

import de.twenty11.skysail.server.core.restlet.ResourceContextId;
import io.skysail.api.links.*;
import io.skysail.api.responses.EntityServerResponse;
import io.skysail.domain.Identifiable;
import io.skysail.server.services.PerformanceTimer;
import io.skysail.server.utils.LinkUtils;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public abstract class RedirectResource<T extends Identifiable> extends SkysailServerResource<T> {

    public RedirectResource() {
        addToContext(ResourceContextId.LINK_TITLE, "Redirection Logic");
    }
    
    protected abstract Class<? extends SkysailServerResource<?>> redirectToResource();

    @Override
    public T getEntity() {
        return null;
    }

    @Override
    public LinkRelation getLinkRelation() {
        return LinkRelation.ALTERNATE;
    }
    
    @Get
    public EntityServerResponse<T> redirectToEntity(Variant variant) {
        Set<PerformanceTimer> perfTimer = getApplication().startPerformanceMonitoring(this.getClass().getSimpleName() + ":redirectToEntity");
        log.info("Request entry point: {} @Get()", this.getClass().getSimpleName());
        if (variant != null) {
            getRequest().getAttributes().put(SKYSAIL_SERVER_RESTLET_VARIANT, variant);
        }
        Link link = LinkUtils.fromResource(getApplication(), redirectToResource());
        getPathSubstitutions().accept(link);
        getResponse().redirectSeeOther(link.getUri());
        getApplication().stopPerformanceMonitoring(perfTimer);
        return new EntityServerResponse<>(getResponse(), null);
    }


}
