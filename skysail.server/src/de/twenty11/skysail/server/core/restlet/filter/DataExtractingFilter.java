package de.twenty11.skysail.server.core.restlet.filter;

import lombok.extern.slf4j.Slf4j;

import org.codehaus.jettison.json.JSONObject;

import de.twenty11.skysail.server.core.restlet.ResponseWrapper;
import de.twenty11.skysail.server.core.restlet.SkysailServerResource;

@Slf4j
public class DataExtractingFilter<R extends SkysailServerResource<T>, T> extends AbstractResourceFilter<R, T> {

    @Override
    public FilterResult doHandle(R resource, ResponseWrapper<T> responseWrapper) {
        log.debug("entering {}#doHandle", this.getClass().getSimpleName());
        T data = resource.getEntity();
        if (data != null) {
            responseWrapper.setEntity(data);
        } else {
            JSONObject asJson = resource.getAsJson();
            responseWrapper.setJson(asJson);
        }
        super.doHandle(resource, responseWrapper);
        return FilterResult.CONTINUE;
    }

}
