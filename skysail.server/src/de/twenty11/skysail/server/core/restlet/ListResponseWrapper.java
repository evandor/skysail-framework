package de.twenty11.skysail.server.core.restlet;

import io.skysail.api.responses.ConstraintViolationsResponse;
import io.skysail.domain.Identifiable;

import java.util.List;

import lombok.*;

import org.restlet.Response;

public class ListResponseWrapper<T extends Identifiable> implements Wrapper {

    @Getter
    @Setter
    private List<T> entity;

    private Response response;

    private ConstraintViolationsResponse<T> constraintViolationsResponse;

    public ListResponseWrapper(List<T> entity) {
        this.entity = entity;
    }

    public ListResponseWrapper() {
    }

    public ListResponseWrapper(Response response) {
        this.response = response;
    }

    public void setResponse(Response response) {
        this.response = response;
    }

    public Response getResponse() {
        return response;
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
