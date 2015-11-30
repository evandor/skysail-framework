package io.skysail.server.restlet.filter;

import java.util.concurrent.atomic.AtomicBoolean;

import org.restlet.*;
import org.restlet.data.*;
import org.restlet.routing.Filter;
import org.restlet.util.Series;
import org.slf4j.*;

/**
 * traces information when the request contains the debug=true parameter.
 * 
 * ThreadSafe
 * 
 */
public class TracerFilter extends Filter {

    private static final Logger logger = LoggerFactory.getLogger(TracerFilter.class);

    private AtomicBoolean doTrace = new AtomicBoolean(false);


    public TracerFilter(Context context) {
        super(context);
    }

    @Override
    protected int beforeHandle(Request request, Response response) {
        //eventHelper.fireEvent(request);
        checkRequest(request);
        traceRequest(request);
        return CONTINUE;
    }

    @Override
    protected void afterHandle(Request request, Response response) {
        trackResponse(response);
    }

    private void checkRequest(Request request) {
        doTrace.set(false);
        Form queryAsForm = request.getOriginalRef().getQueryAsForm();
        if (queryAsForm == null) {
            return;
        }
        String debugFlag = queryAsForm.getFirstValue("debug");
        if (!"true".equals(debugFlag)) {
            return;
        }
        doTrace.set(true);
    }

    private void traceRequest(Request request) {
        if (!doTrace.get()) {
            return;
        }
        logger.info("");
        logger.info("=== debug: request ========================");
        logger.info("{} '{}':", request.getMethod(), request.getResourceRef());

        Series<Cookie> cookies = request.getCookies();
        if (cookies != null && cookies.size() > 0) {
            logger.info(" > Cookies:");
            for (Cookie cookie : cookies) {
                logger.info("   {}: {}", cookie.getName(), cookie.getValue());
            }
        }
        logger.info("");
    }

    private void trackResponse(Response response) {
        if (!doTrace.get()) {
            return;
        }
        logger.info("");
        logger.info("=== debug: response ========================");
        logger.info("Status: {}, Age {}", response.getStatus(), response.getAge());
        logger.info("Allowed methods: {}", response.getAllowedMethods());
        logger.info("Auth Info:       {}", response.getAuthenticationInfo());
        logger.info("Attributes:      {}", response.getAttributes());
        logger.info("Challange Req:   {}", response.getChallengeRequests());
        logger.info("Dimensions:      {}", response.getDimensions());
        logger.info("Location Ref:    {}", response.getLocationRef());
        logger.info("Retry After:     {}", response.getRetryAfter());
        logger.info("Warnings:        {}", response.getWarnings());
        logger.info("");
    }

   
}
