package io.skysail.api.responses;

import org.restlet.Response;

import lombok.Getter;

@Getter
public class EntityServerResponse<T> extends SkysailResponse<T> {

    public EntityServerResponse(Response response, T entity) {
        super(response, entity);
    }

}
