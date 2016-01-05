package de.twenty11.skysail.server.core.restlet;

import org.restlet.Response;

import io.skysail.api.responses.ConstraintViolationsResponse;
import io.skysail.domain.Identifiable;

public class ResponseWrapper<T extends Identifiable> extends AbstractResponseWrapper<T> {

    private T entity;

    public ResponseWrapper(T entity) {
        this.entity = entity;
    }

    public ResponseWrapper() {
    }

    public ResponseWrapper(Response response) {
        this.response = response;
    }

    public T getEntity() {
        return entity;
    }

    public void setEntity(T entity) {
        this.entity = entity;
    }

    @Override
    public void setConstraintViolationResponse(ConstraintViolationsResponse<T> cvr) {
        this.constraintViolationsResponse = cvr;
    }

    public ConstraintViolationsResponse<T> getConstraintViolationsResponse() {
        return constraintViolationsResponse;
    }

    @Override
    public String toString() {
        return "ResponseWrapper for: " + entity;
    }

    @Override
    public void setEntity(Object entity) {
        this.entity = (T)entity;
    }

}
