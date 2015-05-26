package io.skysail.server.converter.wrapper;

import io.skysail.api.domain.Identifiable;
import io.skysail.api.responses.*;

import java.util.*;
import java.util.stream.Collectors;

import org.apache.commons.lang.StringUtils;

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
        FormResponse<?> formResponse = (FormResponse<?>) source;
        Object entity = formResponse.getEntity();
        if (entity instanceof Identifiable) {
            return "../" + ((Identifiable)entity).getId().replace("#", "");
        }
        return "../" + ((FormResponse<?>) source).getId();
    }

    public boolean isConstraintViolationsResponse() {
        return source instanceof ConstraintViolationsResponse;
    }
    
    public String getClasslevelViolationMessage() {
        if (!isConstraintViolationsResponse()) {
            return null;
        }
        Set<ConstraintViolationDetails> violations = ((ConstraintViolationsResponse)source).getViolations();
        String msg = violations.stream().filter(v -> {
            return v.getPropertyPath().equals("");
        }).map(v -> {
            return v.getMessage();
        }).collect(Collectors.joining(", "));
        return StringUtils.isEmpty(msg) ? null : msg;
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
