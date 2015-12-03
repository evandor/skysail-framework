package io.skysail.server.domain.core;

import io.skysail.api.domain.Identifiable;
import io.skysail.api.forms.InputType;
import lombok.*;

/**
 * Part of skysail's core domain: A FieldModel belongs to an entity which belongs to an application.
 *
 */
@Getter
@Setter
@ToString(of = {"id", "type", "inputType"})
public class FieldModel implements Identifiable {

    /** the fields name or identifier, e.g. "title" */
    private String id;

    /** a mandatory field must not be null or empty */
    private boolean mandatory;

    /** the field cannot be changed */
    private boolean readonly;

    /** the fields (java) type, e.g. java.lang.String */
    protected Class<?> type;

    /** text, textarea, radio, checkbox etc... */
    protected InputType inputType;

    /** if set for a FieldModel of type String, indicates that the rendered value should be truncated */
    private Integer truncateTo;

    public FieldModel(String name) {
        this.id = name;
    }

    public String getInputType() {
        return inputType != null ? inputType.name() : "";
    }

    public String getName() {
        return this.id;
    }


}
