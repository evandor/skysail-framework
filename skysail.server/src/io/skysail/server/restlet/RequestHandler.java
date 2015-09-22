package io.skysail.server.restlet;

import io.skysail.api.domain.Identifiable;
import io.skysail.server.app.SkysailApplication;
import io.skysail.server.restlet.filter.*;
import io.skysail.server.restlet.resources.*;

import org.restlet.data.Method;

public class RequestHandler<T extends Identifiable> {

    private SkysailApplication application;

    public RequestHandler(SkysailApplication application) {
        this.application = application;
    }

    /**
     * for now, always return new objects
     *
     * @param method
     *            http method
     * @return the chain
     */
    public AbstractResourceFilter<EntityServerResource<T>, T> createForEntity(Method method) {
        if (method.equals(Method.GET)) {
            return chainForEntityGet();
        } else if (method.equals(Method.DELETE)) {
            return chainForEntityDelete();
        }

        throw new RuntimeException("Method " + method + " is not yet supported");
    }

    public  AbstractResourceFilter<PutEntityServerResource<T>, T> createForFormResponse() {
        return new ExceptionCatchingFilter<PutEntityServerResource<T>, T>(application)
                .calling(new AddApiVersionHeaderFilter<PutEntityServerResource<T>, T>())
                .calling(new ExtractStandardQueryParametersResourceFilter<PutEntityServerResource<T>, T>())
                .calling(new DataExtractingFilter<PutEntityServerResource<T>, T>())
                .calling(new AddLinkheadersFilter<PutEntityServerResource<T>, T>());
    }

    /**
     * for now, always return new objects
     *
     * @return the filter chain
     */
    public AbstractResourceFilter<PostEntityServerResource<T>, T> createForPost() {
        return chainForEntityPost();
    }

    /**
     * for now, always return new objects
     *
     */
    public AbstractResourceFilter<PutEntityServerResource<T>, T> createForPut() {
        return chainForEntityPut();
    }

    public AbstractResourceFilter<PutEntityServerResource<T>, T> createForPatch() {
        return chainForEntityPatch();
    }

    private AbstractResourceFilter<EntityServerResource<T>, T> chainForEntityGet() {
        return new ExceptionCatchingFilter<EntityServerResource<T>, T>(application)
                .calling(new AddApiVersionHeaderFilter<EntityServerResource<T>, T>())
                .calling(new ExtractStandardQueryParametersResourceFilter<EntityServerResource<T>, T>())
                .calling(new DataExtractingFilter<EntityServerResource<T>, T>())
                .calling(new AddLinkheadersFilter<EntityServerResource<T>, T>());
    }

    private AbstractResourceFilter<PostEntityServerResource<T>, T> chainForEntityPost() {
        return new ExceptionCatchingFilter<PostEntityServerResource<T>, T>(application)
                .calling(new ExtractStandardQueryParametersResourceFilter<PostEntityServerResource<T>, T>())
                .calling(new CheckInvalidInputFilter<PostEntityServerResource<T>, T>(application))
                .calling(new FormDataExtractingFilter<PostEntityServerResource<T>, T>())
                .calling(new CheckBusinessViolationsFilter<PostEntityServerResource<T>, T>(application))
                .calling(new OptionalEncryptionFilter<PostEntityServerResource<T>, T>(application))
                .calling(new PersistEntityFilter<PostEntityServerResource<T>, T>(application))
                .calling(new EntityWasAddedFilter<PostEntityServerResource<T>, T>(application))
                .calling(new AddLinkheadersFilter<PostEntityServerResource<T>, T>())
                .calling(new PostRedirectGetFilter<PostEntityServerResource<T>, T>());
    }

    private AbstractResourceFilter<PutEntityServerResource<T>, T> chainForEntityPut() {
        return new ExceptionCatchingFilter<PutEntityServerResource<T>, T>(application)
                .calling(new ExtractStandardQueryParametersResourceFilter<PutEntityServerResource<T>, T>())
                .calling(new CheckInvalidInputFilter<PutEntityServerResource<T>, T>(application))
                .calling(new FormDataExtractingFilter<PutEntityServerResource<T>, T>())
                .calling(new CheckBusinessViolationsFilter<PutEntityServerResource<T>, T>(application))
                .calling(new OptionalEncryptionFilter<PutEntityServerResource<T>, T>(application))
                .calling(new UpdateEntityFilter<PutEntityServerResource<T>, T>())
                // .calling(new LocationHeader)
                .calling(new EntityWasAddedFilter<PutEntityServerResource<T>, T>(application))
                .calling(new AddLinkheadersFilter<PutEntityServerResource<T>, T>())
                .calling(new PutRedirectGetFilter<PutEntityServerResource<T>, T>());
    }

    private AbstractResourceFilter<PutEntityServerResource<T>, T> chainForEntityPatch() {
        return new ExceptionCatchingFilter<PutEntityServerResource<T>, T>(application)
                .calling(new ExtractStandardQueryParametersResourceFilter<PutEntityServerResource<T>, T>())
                .calling(new CheckInvalidInputFilter<PutEntityServerResource<T>, T>(application))
                .calling(new FormDataExtractingFilter<PutEntityServerResource<T>, T>())
                //.calling(new CheckBusinessViolationsFilter<PutEntityServerResource<T>, T>(application))
                .calling(new UpdateEntityFilter<PutEntityServerResource<T>, T>())
                //.calling(new EntityWasAddedFilter<PutEntityServerResource<T>, T>(application))
                .calling(new AddLinkheadersFilter<PutEntityServerResource<T>, T>())
                .calling(new PutRedirectGetFilter<PutEntityServerResource<T>, T>());
    }

    private AbstractResourceFilter<EntityServerResource<T>, T> chainForEntityDelete() {
        return new ExceptionCatchingFilter<EntityServerResource<T>, T>(application)
                .calling(new ExtractStandardQueryParametersResourceFilter<EntityServerResource<T>, T>())
                .calling(new DeleteEntityFilter<EntityServerResource<T>, T>())
                .calling(new EntityWasDeletedFilter<EntityServerResource<T>, T>(application))
                .calling(new DeleteRedirectGetFilter<EntityServerResource<T>, T>());
    }

    public AbstractResourceFilter<PostEntityServerResource<T>, T> newInstance(Method method) {
        if (method.equals(Method.GET)) {
            return new ExceptionCatchingFilter<PostEntityServerResource<T>, T>(application).calling(
                    new DataExtractingFilter<PostEntityServerResource<T>, T>()).calling(
                    new AddLinkheadersFilter<PostEntityServerResource<T>, T>());
        }

        throw new RuntimeException("Method " + method + " is not yet supported");
    }

}
