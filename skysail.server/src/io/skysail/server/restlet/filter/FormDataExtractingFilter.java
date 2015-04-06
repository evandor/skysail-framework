package io.skysail.server.restlet.filter;

import io.skysail.server.restlet.resources.SkysailServerResource;

import java.text.ParseException;

import lombok.extern.slf4j.Slf4j;

import org.restlet.Response;

import de.twenty11.skysail.server.core.restlet.ResponseWrapper;

@Slf4j
public class FormDataExtractingFilter<R extends SkysailServerResource<T>, T> extends AbstractResourceFilter<R, T> {

    @Override
    public FilterResult doHandle(R resource, ResponseWrapper<T> responseWrapper) {
        log.debug("entering {}#doHandle", this.getClass().getSimpleName());
        Response response = responseWrapper.getResponse();
        if (response.getRequest() == null || response.getRequest().getResourceRef() == null) {
            log.warn("request or resourceRef was null");
            return FilterResult.STOP;
        }
        Object data;
        try {
            data = getDataFromRequest(response.getRequest(), resource);
            responseWrapper.setEntity((T) data);
        } catch (ParseException e) {
            throw new RuntimeException("could not parse form", e);
        }

        return super.doHandle(resource, responseWrapper);
    }

}
