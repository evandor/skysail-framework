package io.skysail.server.um.simple.authentication;

import org.apache.shiro.SecurityUtils;
import org.apache.shiro.subject.Subject;
import org.apache.shiro.util.ThreadContext;
import org.restlet.Context;
import org.restlet.Request;
import org.restlet.Response;
import org.restlet.data.CookieSetting;
import org.restlet.ext.crypto.CookieAuthenticator;
import org.restlet.routing.Filter;

import de.twenty11.skysail.server.app.SkysailRootApplication;

public class SimpleDelegationAuthenticator extends CookieAuthenticator {

    // public SimpleDelegationAuthenticator(Context context, boolean optional,
    // String realm, byte[] encryptSecretKey) {
    // super(context, optional, realm, encryptSecretKey);
    // }

    @Override
    protected void afterHandle(Request request, Response response) {
        ThreadContext.remove();
        super.afterHandle(request, response);
    }

    public SimpleDelegationAuthenticator(Context context, String realm, byte[] encryptSecretKey) {
        super(context, realm, encryptSecretKey);
        setIdentifierFormName("username");
        setSecretFormName("password");
        setLoginFormPath(SkysailRootApplication.LOGIN_PATH);
        setLoginPath(SkysailRootApplication.LOGIN_PATH);
        setLogoutPath(SkysailRootApplication.LOGOUT_PATH);
        setOptional(true); // we want anonymous users too
        setVerifier(new SimpleDelegatingVerifier());
    }

    @Override
    protected CookieSetting getCredentialsCookie(Request request, Response response) {
        CookieSetting credentialsCookie = super.getCredentialsCookie(request, response);
        credentialsCookie.setPath("/");
        return credentialsCookie;
    }

    @Override
    protected int logout(Request request, Response response) {
        int result = super.logout(request, response);
        if (Filter.STOP == result) {
            Subject subject = SecurityUtils.getSubject();
            subject.logout();
        }
        return result;
    }
}
