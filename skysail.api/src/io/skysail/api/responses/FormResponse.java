package io.skysail.api.responses;

import org.restlet.Response;

import lombok.Getter;
import lombok.ToString;

/**
 * indicates that the response is to be handled as some kind of input form for an entity of type &lt;T&gt;.
 * 
 * Rendering of a such a response typically results in an html form or the like.
 */
@Getter
@ToString(callSuper = true)
public class FormResponse<T> extends SkysailResponse<T> {

    private String target;
    private String redirectBackTo;
    private String id;

    public FormResponse(Response response, T entity, String target) {
        this(response, entity, target, null);
    }

    public FormResponse(Response response, T entity, String target, String redirectBackTo) {
        this(response, entity, null, target, redirectBackTo);
    }

    public FormResponse(Response response, T entity, String id, String target, String redirectBackTo) {
        super(response, entity);
        this.id = id;
        this.target = target;
        this.redirectBackTo = redirectBackTo;
    }

}
