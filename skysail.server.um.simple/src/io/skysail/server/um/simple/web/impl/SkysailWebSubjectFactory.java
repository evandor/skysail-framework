package io.skysail.server.um.simple.web.impl;

import io.skysail.server.um.simple.web.RestletSubjectContext;

import org.apache.shiro.subject.Subject;
import org.apache.shiro.subject.SubjectContext;
import org.apache.shiro.web.mgt.DefaultWebSubjectFactory;

public class SkysailWebSubjectFactory extends DefaultWebSubjectFactory {

    @Override
    public Subject createSubject(SubjectContext context) {
        if (!(context instanceof RestletSubjectContext)) {
            return super.createSubject(context);
        }
        throw new IllegalStateException("did not expect to end up here...");
        // RestletSubjectContext rsc = (RestletSubjectContext) context;
        // SecurityManager securityManager = rsc.resolveSecurityManager();
        // Session session = rsc.resolveSession();
        // boolean sessionEnabled = rsc.isSessionCreationEnabled();
        // PrincipalCollection principals = rsc.resolvePrincipals();
        // boolean authenticated = rsc.resolveAuthenticated();
        // String host = rsc.resolveHost();
        // Request request = rsc.resolveRequest();
        // Response response = rsc.resolveResponse();
        //
        // return new RestletDelegatingSubject(principals, authenticated, host,
        // session, sessionEnabled, request,
        // response, securityManager);

    }

}
