package io.skysail.server.restlet.filter;

import org.restlet.resource.ResourceException;

import de.twenty11.skysail.server.core.restlet.Wrapper;
import io.skysail.domain.Identifiable;
import io.skysail.server.app.SkysailApplication;
import io.skysail.server.restlet.filter.helper.ExceptionCatchingFilterHelper;
import io.skysail.server.restlet.resources.SkysailServerResource;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class ExceptionCatchingFilter<R extends SkysailServerResource<?>, T extends Identifiable> extends
        AbstractResourceFilter<R, T> {

    private SkysailApplication application;

    public ExceptionCatchingFilter(SkysailApplication application) {
        this.application = application;
    }

    @Override
    public FilterResult doHandle(R resource, Wrapper<T> responseWrapper) {
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
