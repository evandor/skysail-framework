package de.twenty11.skysail.server.core.restlet.filter;

import org.codehaus.jettison.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import de.twenty11.skysail.server.core.restlet.PutEntityServerResource;
import de.twenty11.skysail.server.core.restlet.ResponseWrapper;

public class UpdateEntityFilter<R extends PutEntityServerResource<T>, T> extends AbstractResourceFilter<R, T> {

    private static Logger logger = LoggerFactory.getLogger(UpdateEntityFilter.class);

    @Override
    public FilterResult doHandle(R resource, ResponseWrapper<T> responseWrapper) {
        logger.debug("entering {}#doHandle", this.getClass().getSimpleName());
        T entity = responseWrapper.getEntity();
        if (entity != null) {
            resource.updateEntity(entity);
        } else {
            JSONObject data = responseWrapper.getData();
            resource.updateEntity(data);
        }
        super.doHandle(resource, responseWrapper);
        return FilterResult.CONTINUE;
    }

}
