package io.skysail.api.responses;

import lombok.Getter;

/**
 * Another type of Response is the FormResponse: You'd get it in case of an HTML
 * - Request whenever you want to add an entity or to display an existing one
 * which you'd want to change.
 */
@Getter
public class FormResponse<T> extends SkysailResponse<T> {

    private String target;
    private String redirectBackTo;
    private String id;

    public FormResponse(T entity, String target) {
        this(entity, target, null);
    }

    public FormResponse(T entity, String target, String redirectBackTo) {
        this(entity, null, target, redirectBackTo);
    }

    /**
     * Constructor.
     * 
     * @param entity
     *            e
     * @param id
     *            id
     * @param target
     *            target
     * @param redirectBackTo
     *            redirect
     */
    public FormResponse(T entity, String id, String target, String redirectBackTo) {
        super(entity);
        this.id = id;
        this.target = target;
        this.redirectBackTo = redirectBackTo;
    }

}
