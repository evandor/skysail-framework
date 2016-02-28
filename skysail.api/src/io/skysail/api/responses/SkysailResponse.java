package io.skysail.api.responses;

import org.restlet.Response;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;


/**
 * A skysail server installation responds to (RESTful) Http-Requests by creating
 * responses, which get converted on-the-fly into the desired target format
 * (like html, json, xml, csv, ...).
 * <p>
 * The actual data contained in the response is described by the generic type
 * parameter &lt;T&gt;
 * </p>
 *
 */
@NoArgsConstructor
@Getter
@ToString
public class SkysailResponse<T> {

    private T entity;
    private Response response;

    public SkysailResponse(Response response, T entity) {
        this.response = response;
        this.entity = entity;
    }

    public boolean isForm() {
        return this instanceof FormResponse || this instanceof ConstraintViolationsResponse;
    }
    
    public boolean isRelationTargetList() {
        return this instanceof RelationTargetResponse;
    }
}
