package de.twenty11.skysail.server.core.restlet;

import java.util.List;

import org.restlet.data.Method;

import de.twenty11.skysail.api.structures.graph.Graph;
import de.twenty11.skysail.server.app.SkysailApplication;
import de.twenty11.skysail.server.core.restlet.filter.AbstractResourceFilter;
import de.twenty11.skysail.server.core.restlet.filter.AddApiVersionHeader;
import de.twenty11.skysail.server.core.restlet.filter.AddLinkheadersFilter;
import de.twenty11.skysail.server.core.restlet.filter.AddRequestIdToResourceFilter;
import de.twenty11.skysail.server.core.restlet.filter.CheckBusinessViolationsFilter;
import de.twenty11.skysail.server.core.restlet.filter.CheckFavoritesFilter;
import de.twenty11.skysail.server.core.restlet.filter.CheckInvalidInputFilter;
import de.twenty11.skysail.server.core.restlet.filter.DataExtractingFilter;
import de.twenty11.skysail.server.core.restlet.filter.DeleteEntityFilter;
import de.twenty11.skysail.server.core.restlet.filter.DeleteRedirectGetFilter;
import de.twenty11.skysail.server.core.restlet.filter.EntityWasAddedFilter;
import de.twenty11.skysail.server.core.restlet.filter.EntityWasDeletedFilter;
import de.twenty11.skysail.server.core.restlet.filter.ExceptionCatchingFilter;
import de.twenty11.skysail.server.core.restlet.filter.FormDataExtractingFilter;
import de.twenty11.skysail.server.core.restlet.filter.GetRequestPreFilterHook;
import de.twenty11.skysail.server.core.restlet.filter.OptionalEncryptionFilter;
import de.twenty11.skysail.server.core.restlet.filter.PersistEntityFilter;
import de.twenty11.skysail.server.core.restlet.filter.PostRedirectGetFilter;
import de.twenty11.skysail.server.core.restlet.filter.PutRedirectGetFilter;
import de.twenty11.skysail.server.core.restlet.filter.RedirectFilter;
import de.twenty11.skysail.server.core.restlet.filter.SetExecutionTimeInResponseFilter;
import de.twenty11.skysail.server.core.restlet.filter.UpdateEntityFilter;

public class RequestHandler<T> {

    private SkysailApplication application;

    public RequestHandler(SkysailApplication application) {
        this.application = application;
    }

    /**
     * for now, always return new objects
     * 
     * @param cvf
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
     * @param cvf
     */
    public AbstractResourceFilter<EntityServerResource<T>, T> createForEntity(Method method) {
        if (method.equals(Method.GET)) {
            return chainForEntityGet();
        } else if (method.equals(Method.DELETE)) {
            return chainForEntityDelete();
        }

        throw new RuntimeException("Method " + method + " is not yet supported");
    }

    public AbstractResourceFilter<GraphResource, Graph> createForGraph(Method method) {
        if (method.equals(Method.GET)) {
            return chainForGraphGet();
        }
        throw new RuntimeException("Method " + method + " is not yet supported");
    }

    /**
     * for now, always return new objects
     * 
     * @param cvf
     */
    public AbstractResourceFilter<PostEntityServerResource<T>, T> createForPost() {
        return chainForEntityPost();
    }

    /**
     * for now, always return new objects
     * 
     * @param cvf
     */
    public AbstractResourceFilter<PutEntityServerResource<T>, T> createForPut() {
        return chainForEntityPut();
    }

    // @formatter:off
    private AbstractResourceFilter<ListServerResource<T>, List<T>> chainForListPost() {
        return new ExceptionCatchingFilter<ListServerResource<T>, List<T>>()
                .calling(new AddRequestIdToResourceFilter<ListServerResource<T>, List<T>>())
                .calling(new CheckInvalidInputFilter<ListServerResource<T>, List<T>>())
                .calling(new FormDataExtractingFilter<ListServerResource<T>, List<T>>())
                .calling(new CheckBusinessViolationsFilter<ListServerResource<T>, List<T>>())
                .calling(new PersistEntityFilter<ListServerResource<T>, List<T>>(application));
    }

    private AbstractResourceFilter<ListServerResource<T>, List<T>> chainForListGet() {
        return new ExceptionCatchingFilter<ListServerResource<T>, List<T>>()
                .calling(new AddApiVersionHeader<ListServerResource<T>, List<T>>())
                .calling(new GetRequestPreFilterHook<ListServerResource<T>, List<T>>(application))
                .calling(new AddRequestIdToResourceFilter<ListServerResource<T>, List<T>>())
                .calling(new DataExtractingFilter<ListServerResource<T>, List<T>>())
                .calling(new AddLinkheadersFilter<ListServerResource<T>, List<T>>())
                // .calling(new AddPaginationFilter<ListServerResource<T>,
                // List<T>>())
                .calling(new CheckFavoritesFilter<ListServerResource<T>, List<T>>())
                .calling(new SetExecutionTimeInResponseFilter<ListServerResource<T>, List<T>>())
                .calling(new RedirectFilter<ListServerResource<T>, List<T>>());
    }

    private AbstractResourceFilter<EntityServerResource<T>, T> chainForEntityGet() {
        return new ExceptionCatchingFilter<EntityServerResource<T>, T>()
                .calling(new AddApiVersionHeader<EntityServerResource<T>, T>())
                .calling(new GetRequestPreFilterHook<EntityServerResource<T>, T>(application))
                .calling(new AddRequestIdToResourceFilter<EntityServerResource<T>, T>())
                .calling(new DataExtractingFilter<EntityServerResource<T>, T>())
                .calling(new AddLinkheadersFilter<EntityServerResource<T>, T>());
    }

    private static AbstractResourceFilter<GraphResource, Graph> chainForGraphGet() {
        return new ExceptionCatchingFilter<GraphResource, Graph>()
                .calling(new AddRequestIdToResourceFilter<GraphResource, Graph>())
                .calling(new DataExtractingFilter<GraphResource, Graph>())
                .calling(new AddLinkheadersFilter<GraphResource, Graph>());
    }

    private AbstractResourceFilter<PostEntityServerResource<T>, T> chainForEntityPost() {
        return new ExceptionCatchingFilter<PostEntityServerResource<T>, T>()
                .calling(new AddRequestIdToResourceFilter<PostEntityServerResource<T>, T>())
                .calling(new CheckInvalidInputFilter<PostEntityServerResource<T>, T>(application))
                .calling(new FormDataExtractingFilter<PostEntityServerResource<T>, T>())
                .calling(new CheckBusinessViolationsFilter<PostEntityServerResource<T>, T>())
                .calling(new OptionalEncryptionFilter<PostEntityServerResource<T>, T>(application))
                .calling(new PersistEntityFilter<PostEntityServerResource<T>, T>(application))
                // .calling(new LocationHeader)
                .calling(new EntityWasAddedFilter<PostEntityServerResource<T>, T>(application))
                .calling(new AddLinkheadersFilter<PostEntityServerResource<T>, T>())
                .calling(new PostRedirectGetFilter<PostEntityServerResource<T>, T>());
    }

    private AbstractResourceFilter<PutEntityServerResource<T>, T> chainForEntityPut() {
        return new ExceptionCatchingFilter<PutEntityServerResource<T>, T>()
                .calling(new AddRequestIdToResourceFilter<PutEntityServerResource<T>, T>())
                .calling(new CheckInvalidInputFilter<PutEntityServerResource<T>, T>(application))
                .calling(new FormDataExtractingFilter<PutEntityServerResource<T>, T>())
                .calling(new CheckBusinessViolationsFilter<PutEntityServerResource<T>, T>())
                .calling(new OptionalEncryptionFilter<PutEntityServerResource<T>, T>(application))
                .calling(new UpdateEntityFilter<PutEntityServerResource<T>, T>())
                // .calling(new LocationHeader)
                .calling(new EntityWasAddedFilter<PutEntityServerResource<T>, T>(application))
                .calling(new AddLinkheadersFilter<PutEntityServerResource<T>, T>())
                .calling(new PutRedirectGetFilter<PutEntityServerResource<T>, T>());
    }

    private AbstractResourceFilter<EntityServerResource<T>, T> chainForEntityDelete() {
        return new ExceptionCatchingFilter<EntityServerResource<T>, T>()
                .calling(new AddRequestIdToResourceFilter<EntityServerResource<T>, T>())
                .calling(new DeleteEntityFilter<EntityServerResource<T>, T>())
                .calling(new EntityWasDeletedFilter<EntityServerResource<T>, T>(application))
                .calling(new DeleteRedirectGetFilter<EntityServerResource<T>, T>());
    }

    public AbstractResourceFilter<PostEntityServerResource<T>, T> newInstance(Method method) {
        if (method.equals(Method.GET)) {
            return new ExceptionCatchingFilter<PostEntityServerResource<T>, T>().calling(
                    new DataExtractingFilter<PostEntityServerResource<T>, T>()).calling(
                    new AddLinkheadersFilter<PostEntityServerResource<T>, T>());
        }

        throw new RuntimeException("Method " + method + " is not yet supported");
    }

}
