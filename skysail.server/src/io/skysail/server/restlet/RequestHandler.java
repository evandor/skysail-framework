package io.skysail.server.restlet;

import io.skysail.server.restlet.filter.AbstractResourceFilter;
import io.skysail.server.restlet.filter.AddApiVersionHeaderFilter;
import io.skysail.server.restlet.filter.AddLinkheadersFilter;
import io.skysail.server.restlet.filter.AddRequestIdToResourceFilter;
import io.skysail.server.restlet.filter.CheckBusinessViolationsFilter;
import io.skysail.server.restlet.filter.CheckFavoritesFilter;
import io.skysail.server.restlet.filter.CheckInvalidInputFilter;
import io.skysail.server.restlet.filter.DataExtractingFilter;
import io.skysail.server.restlet.filter.DeleteEntityFilter;
import io.skysail.server.restlet.filter.DeleteRedirectGetFilter;
import io.skysail.server.restlet.filter.EntityWasAddedFilter;
import io.skysail.server.restlet.filter.EntityWasDeletedFilter;
import io.skysail.server.restlet.filter.ExceptionCatchingFilter;
import io.skysail.server.restlet.filter.FormDataExtractingFilter;
import io.skysail.server.restlet.filter.GetRequestPreFilterHook;
import io.skysail.server.restlet.filter.OptionalEncryptionFilter;
import io.skysail.server.restlet.filter.PersistEntityFilter;
import io.skysail.server.restlet.filter.PostRedirectGetFilter;
import io.skysail.server.restlet.filter.PutRedirectGetFilter;
import io.skysail.server.restlet.filter.RedirectFilter;
import io.skysail.server.restlet.filter.SetExecutionTimeInResponseFilter;
import io.skysail.server.restlet.filter.UpdateEntityFilter;
import io.skysail.server.restlet.resources.EntityServerResource;
import io.skysail.server.restlet.resources.ListServerResource;
import io.skysail.server.restlet.resources.PostEntityServerResource;
import io.skysail.server.restlet.resources.PutEntityServerResource;

import java.util.List;

import org.restlet.data.Method;

import de.twenty11.skysail.server.app.SkysailApplication;

public class RequestHandler<T> {

    private SkysailApplication application;

    public RequestHandler(SkysailApplication application) {
        this.application = application;
    }

    /**
     * for now, always return new objects
     * 
     * @param method
     *            http method
     * @return chain
     */
    public synchronized AbstractResourceFilter<ListServerResource<T>, List<T>> createForList(Method method) {
        if (method.equals(Method.GET)) {
            return chainForListGet();
        } else if (method.equals(Method.POST)) {
            return chainForListPost();
        }
        throw new RuntimeException("Method " + method + " is not yet supported");
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
                .calling(new GetRequestPreFilterHook<PutEntityServerResource<T>, T>(application))
                .calling(new AddRequestIdToResourceFilter<PutEntityServerResource<T>, T>())
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

    // @formatter:off
    private AbstractResourceFilter<ListServerResource<T>, List<T>> chainForListPost() {
        return new ExceptionCatchingFilter<ListServerResource<T>, List<T>>(application)
                .calling(new AddRequestIdToResourceFilter<ListServerResource<T>, List<T>>())
                .calling(new CheckInvalidInputFilter<ListServerResource<T>, List<T>>())
                .calling(new FormDataExtractingFilter<ListServerResource<T>, List<T>>())
                .calling(new CheckBusinessViolationsFilter<ListServerResource<T>, List<T>>(application))
                .calling(new PersistEntityFilter<ListServerResource<T>, List<T>>(application));
    }

    private AbstractResourceFilter<ListServerResource<T>, List<T>> chainForListGet() {
        return new ExceptionCatchingFilter<ListServerResource<T>, List<T>>(application)
                .calling(new AddApiVersionHeaderFilter<ListServerResource<T>, List<T>>())
                .calling(new GetRequestPreFilterHook<ListServerResource<T>, List<T>>(application))
                .calling(new AddRequestIdToResourceFilter<ListServerResource<T>, List<T>>())
                .calling(new DataExtractingFilter<ListServerResource<T>, List<T>>())
                .calling(new AddLinkheadersFilter<ListServerResource<T>, List<T>>())
                .calling(new CheckFavoritesFilter<ListServerResource<T>, List<T>>())
                .calling(new SetExecutionTimeInResponseFilter<ListServerResource<T>, List<T>>())
                .calling(new RedirectFilter<ListServerResource<T>, List<T>>());
    }

    private AbstractResourceFilter<EntityServerResource<T>, T> chainForEntityGet() {
        return new ExceptionCatchingFilter<EntityServerResource<T>, T>(application)
                .calling(new AddApiVersionHeaderFilter<EntityServerResource<T>, T>())
                .calling(new GetRequestPreFilterHook<EntityServerResource<T>, T>(application))
                .calling(new AddRequestIdToResourceFilter<EntityServerResource<T>, T>())
                .calling(new DataExtractingFilter<EntityServerResource<T>, T>())
                .calling(new AddLinkheadersFilter<EntityServerResource<T>, T>());
    }

    private AbstractResourceFilter<PostEntityServerResource<T>, T> chainForEntityPost() {
        return new ExceptionCatchingFilter<PostEntityServerResource<T>, T>(application)
                .calling(new AddRequestIdToResourceFilter<PostEntityServerResource<T>, T>())
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
                .calling(new AddRequestIdToResourceFilter<PutEntityServerResource<T>, T>())
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

    private AbstractResourceFilter<EntityServerResource<T>, T> chainForEntityDelete() {
        return new ExceptionCatchingFilter<EntityServerResource<T>, T>(application)
                .calling(new AddRequestIdToResourceFilter<EntityServerResource<T>, T>())
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
