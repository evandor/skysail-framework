package io.skysail.server.um.simple.web.impl;

import java.io.Serializable;

import org.apache.shiro.authc.AuthenticationException;
import org.apache.shiro.authc.AuthenticationInfo;
import org.apache.shiro.authc.AuthenticationListener;
import org.apache.shiro.authc.AuthenticationToken;
import org.apache.shiro.authc.pam.ModularRealmAuthenticator;
import org.apache.shiro.cache.MemoryConstrainedCacheManager;
import org.apache.shiro.realm.Realm;
import org.apache.shiro.session.mgt.SessionKey;
import org.apache.shiro.subject.PrincipalCollection;
import org.apache.shiro.subject.Subject;
import org.apache.shiro.subject.SubjectContext;
import org.apache.shiro.web.mgt.DefaultWebSecurityManager;
import org.restlet.Request;
import org.restlet.Response;

import io.skysail.server.um.simple.web.RestletSubjectContext;
import io.skysail.server.um.simple.web.utils.RestletUtils;

public class SkysailWebSecurityManager extends DefaultWebSecurityManager {

    public SkysailWebSecurityManager() {
        super();
        setSubjectFactory(new SkysailWebSubjectFactory());
        setSessionManager(new SkysailWebSessionManager());
        setCacheManager(new MemoryConstrainedCacheManager());
        ((ModularRealmAuthenticator)getAuthenticator()).getAuthenticationListeners().add(new AuthenticationListener(){

            @Override
            public void onSuccess(AuthenticationToken token, AuthenticationInfo info) {
            }

            @Override
            public void onFailure(AuthenticationToken token, AuthenticationException ae) {
                System.out.println(ae);
            }

            @Override
            public void onLogout(PrincipalCollection principals) {
            }});
    }

    public SkysailWebSecurityManager(Realm singleRealm) {
        this();
        setRealm(singleRealm);
    }

    @Override
    protected SubjectContext createSubjectContext() {
        return new SkysailWebSubjectContext();
    }

    @Override
    protected SubjectContext copy(SubjectContext subjectContext) {
        if (subjectContext instanceof RestletSubjectContext) {
            return new SkysailWebSubjectContext((RestletSubjectContext) subjectContext);
        }
        return super.copy(subjectContext);
    }

    @Override
    public Subject createSubject(SubjectContext subjectContext) {
        // create a copy so we don't modify the argument's backing map:
        SubjectContext context = copy(subjectContext);

        // ensure that the context has a SecurityManager instance, and if not,
        // add one:
        context = ensureSecurityManager(context);

        // Resolve an associated Session (usually based on a referenced session
        // ID), and place it in the context before
        // sending to the SubjectFactory. The SubjectFactory should not need to
        // know how to acquire sessions as the
        // process is often environment specific - better to shield the SF from
        // these details:
        context = resolveSession(context);

        // Similarly, the SubjectFactory should not require any concept of
        // RememberMe - translate that here first
        // if possible before handing off to the SubjectFactory:
        context = resolvePrincipals(context);

        Subject subject = doCreateSubject(context);

        // save this subject for future reference if necessary:
        // (this is needed here in case rememberMe principals were resolved and
        // they need to be stored in the
        // session, so we don't constantly rehydrate the rememberMe
        // PrincipalCollection on every operation).
        // Added in 1.2:
        // save(subject);

        return subject;
    }

    @Override
    protected SessionKey getSessionKey(SubjectContext context) {
        if (RestletUtils.isRestlet(context)) {
            Serializable sessionId = context.getSessionId();
            Request request = RestletUtils.getRequest(context);
            Response response = RestletUtils.getResponse(context);
            return new RestletSessionKey(sessionId, request, response);
        } else {
            return super.getSessionKey(context);

        }
    }

}
