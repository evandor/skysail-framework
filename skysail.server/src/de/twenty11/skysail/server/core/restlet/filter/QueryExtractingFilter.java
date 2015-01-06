package de.twenty11.skysail.server.core.restlet.filter;

import org.restlet.Response;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import de.twenty11.skysail.server.core.restlet.ResponseWrapper;
import de.twenty11.skysail.server.core.restlet.SkysailServerResource;

public class QueryExtractingFilter<R extends SkysailServerResource<T>, T> extends AbstractResourceFilter<R, T> {

    private static Logger logger = LoggerFactory.getLogger(QueryExtractingFilter.class);

    @Override
    public FilterResult doHandle(R resource, Response response, ResponseWrapper<T> responseWrapper) {
        logger.info("entering {}#doHandle", this.getClass().getSimpleName());

        if (response.getRequest() != null && response.getRequest().getOriginalRef() != null) {
            // form = request.getOriginalRef().getQueryAsForm();
        }
        return FilterResult.CONTINUE;
    }

}
