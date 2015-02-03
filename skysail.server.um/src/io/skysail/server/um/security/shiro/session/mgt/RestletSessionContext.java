package io.skysail.server.um.security.shiro.session.mgt;

import io.skysail.server.um.security.shiro.util.RestletRequestPairSource;

import org.apache.shiro.session.mgt.SessionContext;
import org.restlet.Request;
import org.restlet.Response;

public interface RestletSessionContext extends SessionContext, RestletRequestPairSource {

    Request getRestletRequest();

    void setRequest(Request request);

    Response getRestletResponse();

    void setResponse(Response response);
}
