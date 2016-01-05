package de.twenty11.skysail.server.core.restlet;

import java.util.List;

import org.restlet.Response;

import io.skysail.api.responses.ConstraintViolationsResponse;
import io.skysail.domain.Identifiable;
import lombok.Getter;
import lombok.Setter;

public class ListResponseWrapper<T extends Identifiable> extends AbstractResponseWrapper<T> {

    @Getter
    @Setter
    private List<T> entity;

    public ListResponseWrapper(List<T> entity) {
        this.entity = entity;
    }

    public ListResponseWrapper() {
    }

    public ListResponseWrapper(Response response) {
        this.response = response;
    }

    @Override
    public void setConstraintViolationResponse(ConstraintViolationsResponse cvr) {
        this.constraintViolationsResponse = cvr;
    }

    public ConstraintViolationsResponse<T> getConstraintViolationsResponse() {
        return constraintViolationsResponse;
    }

    @Override
    public void setEntity(Object entity) {
        this.entity = (List<T>)entity;
    }
}
