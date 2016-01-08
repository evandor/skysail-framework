package io.skysail.domain.core;

import io.skysail.domain.html.InputType;
import lombok.Getter;
import lombok.Setter;

/**
 * Part of skysail's core domain: A FieldModel belongs to an entity which belongs to an application.
 *
 */
@Getter
@Setter
public class FieldModel {

    /** the fields name or identifier, e.g. "title" */
    private final String id;

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
    
    @Override
    public String toString() {
        //FieldModel(id=fieldModel, type=class java.lang.String, inputType=BUTTON)
        StringBuilder sb = new StringBuilder(this.getClass().getSimpleName()).append("(");
        sb.append("id=").append(id).append(", ");
        sb.append("type=").append(type).append(", ");
        sb.append("inputType=").append(inputType);
        sb.append(")");
        return sb.toString();
    }


}
