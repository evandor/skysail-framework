package de.twenty11.skysail.server.core.restlet;

import org.restlet.Response;

import io.skysail.api.responses.ConstraintViolationsResponse;
import io.skysail.domain.Identifiable;

public interface Wrapper<T extends Identifiable> {

    Response getResponse();

    Object getEntity();

    void setEntity(Object entity);

    void setConstraintViolationResponse(ConstraintViolationsResponse<T> reponse);

}
