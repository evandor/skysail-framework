package de.twenty11.skysail.server.core.restlet.filter;

import lombok.extern.slf4j.Slf4j;

import org.restlet.Response;
import org.restlet.data.Status;
import org.restlet.resource.ResourceException;

import de.twenty11.skysail.server.app.SkysailApplication;
import de.twenty11.skysail.server.core.osgi.EventHelper;
import de.twenty11.skysail.server.core.restlet.ResponseWrapper;
import de.twenty11.skysail.server.core.restlet.SkysailServerResource;

@Slf4j
public class ExceptionCatchingFilter<R extends SkysailServerResource<T>, T> extends AbstractResourceFilter<R, T> {

    private SkysailApplication application;

    public ExceptionCatchingFilter(SkysailApplication application) {
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
            log.error(e.getMessage(), e);
            Response response = responseWrapper.getResponse();
            response.setStatus(Status.SERVER_ERROR_INTERNAL);
            // responseWrapper.setSkysailResponse(new
            // FailureResponse<T>(response, e));
            new EventHelper(application.getEventAdmin())//
                .channel(EventHelper.GUI_MSG)//
                .error(resource.getClass().getSimpleName() + ".saved.failure")//
                .fire();
        }
        return FilterResult.CONTINUE;
    }

}
