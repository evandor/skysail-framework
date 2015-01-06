package de.twenty11.skysail.server.core.restlet.filter;

import org.restlet.Response;
import org.restlet.data.Status;
import org.restlet.resource.ResourceException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import de.twenty11.skysail.server.core.restlet.ResponseWrapper;
import de.twenty11.skysail.server.core.restlet.SkysailServerResource;

public class ExceptionCatchingFilter<R extends SkysailServerResource<T>, T> extends AbstractResourceFilter<R, T> {

    private static Logger logger = LoggerFactory.getLogger(ExceptionCatchingFilter.class);

    @Override
    public FilterResult doHandle(R resource, Response response, ResponseWrapper<T> responseWrapper) {
        logger.debug("entering {}#doHandle", this.getClass().getSimpleName());
        try {
            super.doHandle(resource, response, responseWrapper);
        } catch (ResourceException re) {
        	throw re;
        } catch (Exception e) {
            logger.error(e.getMessage(), e);
            response.setStatus(Status.SERVER_ERROR_INTERNAL);
            //responseWrapper.setSkysailResponse(new FailureResponse<T>(response, e));
        }
        return FilterResult.CONTINUE;
    }

}
