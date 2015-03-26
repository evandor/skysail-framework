package io.skysail.server.um.simple.authentication;

import org.apache.shiro.SecurityUtils;
import org.apache.shiro.cache.Cache;
import org.apache.shiro.cache.CacheManager;
import org.apache.shiro.subject.Subject;
import org.apache.shiro.util.ThreadContext;
import org.restlet.Context;
import org.restlet.Request;
import org.restlet.Response;
import org.restlet.data.CookieSetting;
import org.restlet.ext.crypto.CookieAuthenticator;
import org.restlet.routing.Filter;

import de.twenty11.skysail.server.app.SkysailRootApplication;

/**
 * Overwriting the restlet cookie authenticator to provide a more specific
 * implementation.
 * 
 * <p>
 * Optional is set to true to allow anonymous access, the credentials cookie
 * path is set to "/" and a verifier is set.
 * </p>
 *
 */
public class SkysailCookieAuthenticator extends CookieAuthenticator {

    private CacheManager cacheManager;

    public SkysailCookieAuthenticator(Context context, String realm, byte[] encryptSecretKey, CacheManager cacheManager) {
        super(context, realm, encryptSecretKey);
        this.cacheManager = cacheManager;

        setIdentifierFormName("username");
        setSecretFormName("password");
        setLoginFormPath(SkysailRootApplication.LOGIN_PATH);
        setLoginPath(SkysailRootApplication.LOGIN_PATH);
        setLogoutPath(SkysailRootApplication.LOGOUT_PATH);
        setOptional(true); // we want anonymous users too
        setVerifier(new SimpleDelegatingVerifier());
    }

    @Override
    protected void afterHandle(Request request, Response response) {
        ThreadContext.remove();
        super.afterHandle(request, response);
    }

    @Override
    protected CookieSetting getCredentialsCookie(Request request, Response response) {
        CookieSetting credentialsCookie = super.getCredentialsCookie(request, response);
        credentialsCookie.setPath("/");
        return credentialsCookie;
    }

    @Override
    protected int logout(Request request, Response response) {
        CookieSetting credentialsCookie = getCredentialsCookie(request,
                response);
        String value = credentialsCookie.getValue();
        int result = super.logout(request, response);
        if (cacheManager != null) {
            Cache<Object, Object> cache = cacheManager.getCache(SkysailHashedCredentialsMatcher.CREDENTIALS_CACHE);
            // need to find the current value: 
            //cache.remove(value);
            // instead: remove all for now
            cache.clear();
        }
        if (Filter.STOP == result) {
            Subject subject = SecurityUtils.getSubject();
            subject.logout();
        }
        return result;
    }
}
