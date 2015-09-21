package de.twenty11.skysail.server.core.restlet;

import io.skysail.api.responses.ConstraintViolationsResponse;

import org.restlet.Response;

public interface Wrapper {

    Response getResponse();

    Object getEntity();

    void setEntity(Object entity);

    void setConstraintViolationResponse(ConstraintViolationsResponse reponse);

}
