package io.skysail.api.responses;

import java.util.List;

import org.restlet.Response;

import lombok.Getter;

@Getter
public class ListServerResponse<T> extends SkysailResponse<List<T>> {

    public ListServerResponse(Response response, List<T> entity) {
        super(response, entity);
    }

}
