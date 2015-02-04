package io.skysail.server.um.security.shiro.subject.support;

import io.skysail.server.um.security.shiro.session.mgt.RestletSessionContext;
import io.skysail.server.um.security.shiro.session.mgt.SkysailWebSessionContext;
import io.skysail.server.um.security.shiro.subject.RestletSubject;

import org.apache.shiro.mgt.SecurityManager;
import org.apache.shiro.session.Session;
import org.apache.shiro.session.mgt.SessionContext;
import org.apache.shiro.subject.PrincipalCollection;
import org.apache.shiro.subject.support.DelegatingSubject;
import org.apache.shiro.util.StringUtils;
import org.restlet.Request;
import org.restlet.Response;

public class RestletDelegatingSubject extends DelegatingSubject implements RestletSubject {

    private Request request;
    private Response response;

    public RestletDelegatingSubject(PrincipalCollection principals, boolean authenticated, String host,
            Session session, boolean sessionEnabled, Request request, Response response, SecurityManager securityManager) {
        super(principals, authenticated, host, session, sessionEnabled, securityManager);
        this.request = request;
        this.response = response;
    }

    @Override
    public Request getRestletRequest() {
        return request;
    }

    @Override
    public Response getRestletResponse() {
        return response;
    }

    @Override
    protected SessionContext createSessionContext() {
        RestletSessionContext wsc = new SkysailWebSessionContext();
        String host = getHost();
        if (StringUtils.hasText(host)) {
            wsc.setHost(host);
        }
        wsc.setRequest(this.request);
        wsc.setResponse(this.response);
        return wsc;
    }

    @Override
    public String toString() {
        StringBuffer sb = new StringBuffer("Principal: ");
        sb.append(getPrincipal() != null ? getPrincipal().toString() : "<none>");
        sb.append(", authenticated: ").append(isAuthenticated());
        return sb.toString();
    }
}
