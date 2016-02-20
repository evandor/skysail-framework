package io.skysail.api.responses;

import java.util.List;

import org.restlet.Response;

import lombok.Getter;

/**
 * indicates that the response is to be handled as a list of entities of type <T>.
 * 
 * Rendering of a such a response typically results in some kind of paginated table view,
 * or a "infinite" list.
 */
@Getter
public class ListServerResponse<T> extends SkysailResponse<List<T>> {

    public ListServerResponse(Response response, List<T> list) {
        super(response, list);
    }

}
