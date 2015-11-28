package io.skysail.api.responses;

import lombok.NoArgsConstructor;


/**
 * A skysail server installation responds to (RESTful) Http-Requests by creating
 * responses, which get converted on-the-fly into the desired target format
 * (like html, json, xml, csv, ...).
 * <p>
 * The actual data contained in the response is described by the generic type
 * parameter &lt;T&gt;; there are no formal restrictions on that type, but you
 * have to keep in mind that it is supposed to be serializable into formats as
 * JSON, XML and the like.
 * </p>
 *
 * @param <T>
 *            The type of the generic entity
 */
@NoArgsConstructor
public class SkysailResponse<T> {

    private T entity;

    /**
     * constructor.
     *
     * @param entity
     *            the parameterized entity
     */
    public SkysailResponse(T entity) {
        this.entity = entity;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append(this.getClass().getSimpleName()).append(": ");
        return sb.toString();
    }

    public boolean isForm() {
        return this instanceof FormResponse || this instanceof ConstraintViolationsResponse;
    }

    public T getEntity() {
        return entity;
    }

}
