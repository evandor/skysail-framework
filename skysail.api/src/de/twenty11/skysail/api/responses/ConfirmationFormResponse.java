package de.twenty11.skysail.api.responses;


/**
 * A ConfirmationFormResponse represents a form, providing its data as read-only fields, not meant to be 
 * changed, to inform you about what are you about to post.
 */
public class ConfirmationFormResponse<T> extends FormResponse<T> {

    public ConfirmationFormResponse(T entity, String target) {
        super(entity, target);
    }

    private String target;

    public String getTarget() {
        return target;
    }

}
