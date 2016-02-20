package io.skysail.server.restlet.resources;

import java.util.List;
import java.util.Set;

import org.restlet.data.Method;
import org.restlet.representation.Variant;
import org.restlet.resource.Get;

import de.twenty11.skysail.server.core.restlet.ResourceContextId;
import io.skysail.api.links.LinkRelation;
import io.skysail.api.responses.RelationTargetResponse;
import io.skysail.domain.Identifiable;
import io.skysail.server.restlet.RelationTargetListRequestHandler;
import io.skysail.server.restlet.response.ListResponseWrapper;
import io.skysail.server.services.PerformanceTimer;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public abstract class PostRelationResource<FROM extends Identifiable, TO extends Identifiable>
        extends SkysailServerResource<List<TO>> {

    private RelationTargetListRequestHandler<FROM, TO> requestHandler;

    /**
     * Default constructor without associatedEntityServerResource.
     */
    public PostRelationResource() {
        requestHandler = new RelationTargetListRequestHandler<FROM, TO>(null);
        addToContext(ResourceContextId.LINK_TITLE, "relations");
    }

    @Get("html|json|yaml|xml|csv|timeline|standalone")
    public RelationTargetResponse<TO> getEntities(Variant variant) {
        Set<PerformanceTimer> perfTimer = getApplication()
                .startPerformanceMonitoring(this.getClass().getSimpleName() + ":getEntities");
        log.info("Request entry point: {} @Get({})", this.getClass().getSimpleName(), variant);
        List<TO> response = listTargetEntities();
        getApplication().stopPerformanceMonitoring(perfTimer);
        return new RelationTargetResponse<>(getResponse(), response);
    }

    @SuppressWarnings("unchecked")
    private final List<TO> listTargetEntities() {
        ListResponseWrapper<?> responseWrapper = requestHandler.createForRelationTargetList(Method.GET).handleList(this,
                getResponse());
        return (List<TO>) responseWrapper.getEntity();
    }

    @Override
    public LinkRelation getLinkRelation() {
        return LinkRelation.RELATED;
    }

}
