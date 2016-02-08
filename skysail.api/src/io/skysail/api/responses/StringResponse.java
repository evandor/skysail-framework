package io.skysail.api.responses;

import org.restlet.Response;

public class StringResponse<T> extends SkysailResponse<T> {

    public StringResponse(Response response, T entity) {
        super(response, entity);
    }

}
