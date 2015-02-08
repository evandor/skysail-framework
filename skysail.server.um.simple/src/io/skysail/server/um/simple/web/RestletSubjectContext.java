package io.skysail.server.um.simple.web;

import org.apache.shiro.subject.SubjectContext;
import org.restlet.Request;
import org.restlet.Response;

/**
 * A {@code RestSubjectContext} is a {@link SubjectContext} that additionally
 * provides for type-safe methods to set and retrieve a (restlet)
 * {@link Request} and {@link Response}.
 */
public interface RestletSubjectContext extends SubjectContext, RestletRequestPairSource {

    Request resolveRequest();

    Response resolveResponse();

    void setRequest(Request request);

    void setResponse(Response response);
}
