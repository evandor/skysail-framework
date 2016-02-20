package io.skysail.server.restlet;

import org.restlet.data.Method;

import io.skysail.domain.Identifiable;
import io.skysail.server.app.SkysailApplication;
import io.skysail.server.restlet.filter.*;
import io.skysail.server.restlet.resources.PostRelationResource;

public class RelationTargetListRequestHandler<FROM extends Identifiable, TO extends Identifiable> {

    private SkysailApplication application;

    public RelationTargetListRequestHandler(SkysailApplication application) {
        this.application = application;
    }

   
    public synchronized AbstractResourceFilter<PostRelationResource<FROM,TO>, TO> createForRelationTargetList(Method method) {
        if (method.equals(Method.GET)) {
            return chainForRelationTargetListGet();
        } else if (method.equals(Method.POST)) {
        }
        throw new RuntimeException("Method " + method + " is not yet supported");
    }

   
    private AbstractResourceFilter<PostRelationResource<FROM,TO>, TO> chainForRelationTargetListGet() {
        return new ExceptionCatchingFilter<PostRelationResource<FROM,TO>, TO>(application)
                .calling(new AddApiVersionHeaderFilter<>())
                .calling(new ExtractStandardQueryParametersResourceFilter<>())
                .calling(new DataExtractingFilter<>())
                .calling(new AddLinkheadersFilter<>())
                .calling(new SetExecutionTimeInResponseFilter<>())
                .calling(new RedirectFilter<>());
    }

}
