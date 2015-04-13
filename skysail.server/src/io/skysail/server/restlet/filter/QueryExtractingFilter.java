//package io.skysail.server.restlet.filter;
//
//import io.skysail.server.restlet.resources.SkysailServerResource;
//
//import org.restlet.Response;
//import org.slf4j.Logger;
//import org.slf4j.LoggerFactory;
//
//import de.twenty11.skysail.server.core.restlet.ResponseWrapper;
//
//public class QueryExtractingFilter<R extends SkysailServerResource<T>, T> extends AbstractResourceFilter<R, T> {
//
//    private static Logger logger = LoggerFactory.getLogger(QueryExtractingFilter.class);
//
//    @Override
//    public FilterResult doHandle(R resource, ResponseWrapper<T> responseWrapper) {
//        logger.info("entering {}#doHandle", this.getClass().getSimpleName());
//        Response response = responseWrapper.getResponse();
//        if (response.getRequest() != null && response.getRequest().getOriginalRef() != null) {
//            // form = request.getOriginalRef().getQueryAsForm();
//        }
//        return FilterResult.CONTINUE;
//    }
//
//}
