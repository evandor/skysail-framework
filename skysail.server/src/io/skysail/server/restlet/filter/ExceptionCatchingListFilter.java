package io.skysail.server.restlet.filter;

import io.skysail.api.domain.Identifiable;
import io.skysail.server.app.SkysailApplication;
import io.skysail.server.restlet.filter.helper.ExceptionCatchingFilterHelper;
import io.skysail.server.restlet.resources.SkysailServerResource;
import lombok.extern.slf4j.Slf4j;

import org.restlet.resource.ResourceException;

import de.twenty11.skysail.server.core.restlet.ResponseWrapper;

@Slf4j
public class ExceptionCatchingListFilter<R extends SkysailServerResource<?>, T extends Identifiable, S extends java.util.List<T>>
        extends AbstractListResourceFilter<R, T, S> {

    private SkysailApplication application;

    public ExceptionCatchingListFilter(SkysailApplication application) {
        this.application = application;
    }

    @Override
    public FilterResult doHandle(R resource, ResponseWrapper<T> responseWrapper) {
        log.debug("entering {}#doHandle", this.getClass().getSimpleName());
        try {
            super.doHandle(resource, responseWrapper);
        } catch (ResourceException re) {
            throw re;
        } catch (Exception e) {
            ExceptionCatchingFilterHelper.handleError(e, application, responseWrapper, resource.getClass());
        }
        return FilterResult.CONTINUE;
    }

}
