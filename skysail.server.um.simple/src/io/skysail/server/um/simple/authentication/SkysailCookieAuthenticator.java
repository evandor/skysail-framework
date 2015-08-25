package io.skysail.server.um.simple.authentication;

import lombok.extern.slf4j.Slf4j;

import org.apache.shiro.SecurityUtils;
import org.apache.shiro.cache.*;
import org.apache.shiro.subject.Subject;
import org.apache.shiro.util.ThreadContext;
import org.restlet.*;
import org.restlet.data.*;
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
@Slf4j
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
    protected boolean authenticate(Request request, Response response) {
        // Restore credentials from the cookie
        log.debug("getting cookie with name {}", getCookieName());
        Cookie credentialsCookie = request.getCookies().getFirst(
                getCookieName());

        if (credentialsCookie != null) {
            if (byPassIfPublicUrl(request)) {
                return false;// super.authenticate(request, response);
            }
            log.debug("providing cookie with value '{}'", credentialsCookie.getValue());
            request.setChallengeResponse(parseCredentials(credentialsCookie
                    .getValue()));
        }
        return super.authenticate(request, response);
    }

    private boolean byPassIfPublicUrl(Request request) {
        return "anonymous".equals(request.getOriginalRef().getQueryAsForm().getFirstValue("_asUser"));
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
            response.redirectSeeOther("/");
        }
        return result;
    }
}
