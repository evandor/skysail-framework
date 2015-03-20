package de.twenty11.skysail.server.core.restlet;

import io.skysail.api.responses.ConstraintViolationsResponse;

import org.codehaus.jettison.json.JSONObject;
import org.restlet.Response;

public class ResponseWrapper<T> {

    private T entity;

    /**
     * an internal request id added by a filter to track request and response
     * information
     */
    private Long requestId;

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

    public void setResponse(Response response) {
        this.response = response;
    }

    public Response getResponse() {
        return response;
    }

    public void setConstraintViolationResponse(ConstraintViolationsResponse<T> cvr) {
        this.constraintViolationsResponse = cvr;
    }

    public ConstraintViolationsResponse<T> getConstraintViolationsResponse() {
        return constraintViolationsResponse;
    }

    public void setRequestId(Long requestId) {
        this.requestId = requestId;
    }

    public Long getRequestId() {
        return requestId;
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
}
