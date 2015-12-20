package io.skysail.server.restlet;

import io.skysail.domain.Identifiable;
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
                .calling(new AddApiVersionHeaderFilter<>())
                .calling(new ExtractStandardQueryParametersResourceFilter<>())
                .calling(new DataExtractingFilter<>())
                .calling(new AddLinkheadersFilter<>());
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
                .calling(new AddApiVersionHeaderFilter<>())
                .calling(new ExtractStandardQueryParametersResourceFilter<>())
                .calling(new DataExtractingFilter<>())
                .calling(new AddReferrerCookieFilter<>())
                .calling(new AddLinkheadersFilter<>());
    }

    private AbstractResourceFilter<PostEntityServerResource<T>, T> chainForEntityPost() {
        return new ExceptionCatchingFilter<PostEntityServerResource<T>, T>(application)
                .calling(new ExtractStandardQueryParametersResourceFilter<>())
                .calling(new CheckInvalidInputFilter<>(application))
                .calling(new FormDataExtractingFilter<>())
                .calling(new CheckBusinessViolationsFilter<>(application))
                .calling(new OptionalEncryptionFilter<>(application))
                .calling(new PersistEntityFilter<>(application))
                .calling(new EntityWasAddedFilter<>(application))
                .calling(new AddLinkheadersFilter<>())
                .calling(new PostRedirectGetFilter<>());
    }

    private AbstractResourceFilter<PutEntityServerResource<T>, T> chainForEntityPut() {
        return new ExceptionCatchingFilter<PutEntityServerResource<T>, T>(application)
                .calling(new ExtractStandardQueryParametersResourceFilter<>())
                .calling(new CheckInvalidInputFilter<>(application))
                .calling(new FormDataExtractingFilter<>())
                .calling(new CheckBusinessViolationsFilter<>(application))
                .calling(new OptionalEncryptionFilter<>(application))
                .calling(new UpdateEntityFilter<>())
                // .calling(new LocationHeader)
                .calling(new EntityWasAddedFilter<>(application))
                .calling(new AddLinkheadersFilter<>())
                .calling(new PutRedirectGetFilter<>());
    }

    private AbstractResourceFilter<PutEntityServerResource<T>, T> chainForEntityPatch() {
        return new ExceptionCatchingFilter<PutEntityServerResource<T>, T>(application)
                .calling(new ExtractStandardQueryParametersResourceFilter<>())
                .calling(new CheckInvalidInputFilter<>(application))
                .calling(new FormDataExtractingFilter<>())
                //.calling(new CheckBusinessViolationsFilter<>(application))
                .calling(new UpdateEntityFilter<>())
                //.calling(new EntityWasAddedFilter<>(application))
                .calling(new AddLinkheadersFilter<>())
                .calling(new PutRedirectGetFilter<>());
    }

    private AbstractResourceFilter<EntityServerResource<T>, T> chainForEntityDelete() {
        return new ExceptionCatchingFilter<EntityServerResource<T>, T>(application)
                .calling(new ExtractStandardQueryParametersResourceFilter<>())
                .calling(new DeleteEntityFilter<>())
                .calling(new EntityWasDeletedFilter<>(application))
                .calling(new DeleteRedirectGetFilter<>());
    }

    public AbstractResourceFilter<PostEntityServerResource<T>, T> newInstance(Method method) {
        if (method.equals(Method.GET)) {
            return new ExceptionCatchingFilter<PostEntityServerResource<T>, T>(application)
                .calling(new DataExtractingFilter<>())
                .calling(new AddLinkheadersFilter<>());
        }

        throw new RuntimeException("Method " + method + " is not yet supported");
    }

}
