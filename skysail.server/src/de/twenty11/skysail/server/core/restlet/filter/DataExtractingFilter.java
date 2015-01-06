package de.twenty11.skysail.server.core.restlet.filter;

import lombok.extern.slf4j.Slf4j;

import org.restlet.Response;

import de.twenty11.skysail.server.core.restlet.ResponseWrapper;
import de.twenty11.skysail.server.core.restlet.SkysailServerResource;

@Slf4j
public class DataExtractingFilter<R extends SkysailServerResource<T>, T> extends AbstractResourceFilter<R, T> {

    @Override
    public FilterResult doHandle(R resource, Response response, ResponseWrapper<T> responseWrapper) {
        log.debug("entering {}#doHandle", this.getClass().getSimpleName());
        T data = resource.getData();
        responseWrapper.setEntity(data);
        super.doHandle(resource, response, responseWrapper);
        return FilterResult.CONTINUE;
    }

}
