package io.skysail.server.um.simple.web.impl;

import io.skysail.server.um.simple.web.RestletRequestPairSource;

import java.io.Serializable;

import org.apache.shiro.session.mgt.DefaultSessionKey;
import org.restlet.Request;
import org.restlet.Response;

public class RestletSessionKey extends DefaultSessionKey implements RestletRequestPairSource {

    private final Request request;
    private final Response response;

    public RestletSessionKey(Request request, Response response) {
        if (request == null) {
            throw new NullPointerException("request argument cannot be null.");
        }
        if (response == null) {
            throw new NullPointerException("response argument cannot be null.");
        }
        this.request = request;
        this.response = response;
    }

    public RestletSessionKey(Serializable sessionId, Request request, Response response) {
        this(request, response);
        setSessionId(sessionId);
    }

    public Request getRestletRequest() {
        return request;
    }

    public Response getRestletResponse() {
        return response;
    }
}
