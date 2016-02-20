package io.skysail.api.responses;

import java.util.List;

import org.restlet.Response;

import lombok.Getter;

/**
 * indicates that the response is to be handled as a list of potential targets of type <T>
 * in a one-to-many relation.
 * 
 * Rendering of a such a response typically results in a multiselect input element.
 */
@Getter
public class RelationTargetResponse<T> extends SkysailResponse<List<T>> {

    public RelationTargetResponse(Response response, List<T> list) {
        super(response, list);
    }

}
