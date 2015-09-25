package io.skysail.server.restlet;

import io.skysail.api.domain.Identifiable;
import io.skysail.server.app.SkysailApplication;
import io.skysail.server.restlet.filter.*;
import io.skysail.server.restlet.resources.ListServerResource;

import org.restlet.data.Method;

public class ListRequestHandler<T extends Identifiable> {

    private SkysailApplication application;

    public ListRequestHandler(SkysailApplication application) {
        this.application = application;
    }

    /**
     * for now, always return new objects
     *
     * @param method
     *            http method
     * @return chain
     */
    public synchronized AbstractResourceFilter<ListServerResource<?>, T> createForList(Method method) {
        if (method.equals(Method.GET)) {
            return chainForListGet();
        } else if (method.equals(Method.POST)) {
            return chainForListPost();
        }
        throw new RuntimeException("Method " + method + " is not yet supported");
    }

    private AbstractResourceFilter<ListServerResource<?>, T> chainForListPost() {
        return new ExceptionCatchingFilter<ListServerResource<?>, T>(application)
                .calling(new ExtractStandardQueryParametersResourceFilter<ListServerResource<?>, T>())
                .calling(new CheckInvalidInputFilter<ListServerResource<?>, T>())
                .calling(new FormDataExtractingFilter<ListServerResource<?>, T>())
                .calling(new CheckBusinessViolationsFilter<ListServerResource<?>, T>(application))
                .calling(new PersistEntityFilter<ListServerResource<?>, T>(application))
                ;
    }

    private AbstractResourceFilter<ListServerResource<?>, T> chainForListGet() {
        return new ExceptionCatchingFilter<ListServerResource<?>, T>(application)
                .calling(new AddApiVersionHeaderFilter<ListServerResource<?>, T>())
                .calling(new ExtractStandardQueryParametersResourceFilter<ListServerResource<?>, T>())
                .calling(new DataExtractingFilter<ListServerResource<?>, T>())
                .calling(new AddLinkheadersFilter<ListServerResource<?>, T>())
                .calling(new SetExecutionTimeInResponseFilter<ListServerResource<?>, T>())
                .calling(new RedirectFilter<ListServerResource<?>, T>());
    }

}
