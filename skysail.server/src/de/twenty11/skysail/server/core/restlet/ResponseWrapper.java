package de.twenty11.skysail.server.core.restlet;

import io.skysail.api.domain.Identifiable;
import io.skysail.api.responses.ConstraintViolationsResponse;

import org.codehaus.jettison.json.JSONObject;
import org.restlet.Response;

public class ResponseWrapper<T extends Identifiable> implements Wrapper {

    private T entity;

    private Response response;

    private ConstraintViolationsResponse<T> constraintViolationsResponse;

    private JSONObject data;

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

//    public void setListEntity(List<T> listEntity) {
//        this.entity = listEntity;
//    }

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
    public String toString() {
        return "ResponseWrapper for: " + entity;
    }

    public void setJson(JSONObject data) {
        this.data = data;
    }

    public JSONObject getData() {
        return data;
    }

    @Override
    public void setEntity(Object entity) {
        this.entity = (T)entity;
    }

}
