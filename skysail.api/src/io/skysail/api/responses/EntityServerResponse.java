package io.skysail.api.responses;

import lombok.Getter;

@Getter
public class EntityServerResponse<T> extends SkysailResponse<T> {

    public EntityServerResponse(T entity) {
        super(entity);
    }

}
