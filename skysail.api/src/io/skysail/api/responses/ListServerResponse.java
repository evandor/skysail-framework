package io.skysail.api.responses;

import java.util.List;

import lombok.Getter;

@Getter
public class ListServerResponse<T> extends SkysailResponse<List<T>> {

    public ListServerResponse(List<T> entity) {
        super(entity);
    }

}
