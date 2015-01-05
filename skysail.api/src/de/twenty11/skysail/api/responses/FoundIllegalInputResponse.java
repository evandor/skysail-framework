package de.twenty11.skysail.api.responses;

import org.restlet.data.Reference;

import com.fasterxml.jackson.annotation.JsonIgnore;

/**
 * The kind of response to be used when the skysail server finds illegal input from the user (e.g. javascript for
 * cross-side scripting attacks or the like).
 */
public class FoundIllegalInputResponse<T> extends SkysailResponse<T> {

    @JsonIgnore
    private Reference actionReference;

    public FoundIllegalInputResponse(Reference actionReference) {
        super(null);

        this.actionReference = actionReference;
    }

    public Reference getActionReference() {
        return actionReference;
    }
}
