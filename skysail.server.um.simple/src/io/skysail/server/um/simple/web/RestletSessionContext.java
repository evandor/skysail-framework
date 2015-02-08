package io.skysail.server.um.simple.web;

import org.apache.shiro.session.mgt.SessionContext;
import org.restlet.Request;
import org.restlet.Response;

public interface RestletSessionContext extends SessionContext, RestletRequestPairSource {

    Request getRestletRequest();

    void setRequest(Request request);

    Response getRestletResponse();

    void setResponse(Response response);
}
