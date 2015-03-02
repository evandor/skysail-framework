package io.skysail.server.converter.wrapper;

import java.util.List;

import de.twenty11.skysail.api.responses.ConstraintViolationsResponse;
import de.twenty11.skysail.api.responses.FormResponse;
import de.twenty11.skysail.api.responses.SkysailResponse;

public class STSourceWrapper {

    private Object source;

    public STSourceWrapper(Object source) {
        this.source = source;
    }

    public String getEntityType() {
        return this.source.getClass().getName();
    }

    public boolean isForm() {
        if (source instanceof FormResponse) {
            return ((SkysailResponse<?>) source).isForm();
        }
        if (source instanceof ConstraintViolationsResponse) {
            return ((ConstraintViolationsResponse<?>) source).isForm();
        }
        return false;
    }

    public boolean isList() {
        return source instanceof List;
    }

    public String getFormTarget() {
        if (!(source instanceof FormResponse)) {
            return null;
        }
        return ((FormResponse<?>) source).getTarget();
    }

    public String getDeleteFormTarget() {
        if (!(source instanceof FormResponse)) {
            return null;
        }
        return "../" + ((FormResponse<?>) source).getId();
    }

    public boolean isConstraintViolationsResponse() {
        return source instanceof ConstraintViolationsResponse;
    }

    public Object getEntity() {
        return source;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder(this.getClass().getSimpleName()).append(": ")
                .append(source.getClass().getName()).append(", isForm: ").append(isForm());
        if (source instanceof SkysailResponse) {
            Object entity = ((SkysailResponse<?>) source).getEntity();
            sb.append("<br>Entity: ").append(entity == null ? "null" : entity.toString());
        }
        return sb.toString();
    }

}
