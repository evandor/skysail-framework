package de.twenty11.skysail.api.responses;

import org.codehaus.jettison.json.JSONObject;

/**
 * A last type of Response is the FormResponse: You'd get it in case of an HTML
 * - Request whenever you want to add an entity or to display an existing one
 * which you'd want to change.
 */
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

    public FormResponse(JSONObject entity, Class<?> cls, String id, String target, String redirectBackTo) {
        super(entity, cls);
        this.id = id;
        this.target = target;
        this.redirectBackTo = redirectBackTo;
    }

    public String getTarget() {
        return target;
    }

    public String getRedirectBackTo() {
        return redirectBackTo;
    }

    public String getId() {
        return id;
    }

}
