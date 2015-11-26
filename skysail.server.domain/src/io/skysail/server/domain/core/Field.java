package io.skysail.server.domain.core;

import io.skysail.api.domain.Identifiable;
import io.skysail.api.forms.InputType;
import lombok.*;

/**
 * Part of skysail's core domain: A Field belongs to an entity which belongs to an application.
 *
 */
@Getter
@Setter
@ToString(of = {"id", "type", "inputType"})
public class Field implements Identifiable {

    /** the fields name or identifier, e.g. "title" */
    private String id;

    /** a mandatory field must not be null or empty */
    private boolean mandatory;

    /** the field cannot be changed */
    private boolean readonly;

    /** the fields (java) type, e.g. java.lang.String */
    private Class<?> type;

    /** text, textarea, radio, checkbox etc... */
    private InputType inputType;

    /** if set for a Field of type String, indicates that the rendered value should be truncated */
    private Integer truncateTo;

    /** if set to true, the field will be rendered in such a way that the form will be submitted when clicking */
    // TODO move GUI-specific stuff to model extension?
    private boolean submitField;

    public Field(String name) {
        this.id = name;
    }


}
