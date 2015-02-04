package io.skysail.server.um.security.shiro.session.mgt;

import io.skysail.server.um.security.shiro.util.RestletUtils;

import java.io.Serializable;

import org.apache.shiro.session.ExpiredSessionException;
import org.apache.shiro.session.InvalidSessionException;
import org.apache.shiro.session.Session;
import org.apache.shiro.session.mgt.DefaultSessionManager;
import org.apache.shiro.session.mgt.SessionContext;
import org.apache.shiro.session.mgt.SessionKey;
import org.apache.shiro.web.servlet.ShiroHttpServletRequest;
import org.apache.shiro.web.servlet.ShiroHttpSession;
import org.apache.shiro.web.session.mgt.WebSessionManager;
import org.restlet.Request;
import org.restlet.Response;
import org.restlet.data.CookieSetting;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class SkysailWebSessionManager extends DefaultSessionManager implements WebSessionManager {

    private static Logger logger = LoggerFactory.getLogger(SkysailWebSessionManager.class);

    public SkysailWebSessionManager() {
        logger.info("creating new SkysailWebSessionManager");
    }

    @Override
    public boolean isServletContainerSessions() {
        return false;
    }

    /**
     * Stores the Session's ID, usually as a Cookie, to associate with future
     * requests.
     * 
     * @param session
     *            the session that was just {@link #createSession created}.
     */
    @Override
    protected void onStart(Session session, SessionContext context) {
        Request request = RestletUtils.getRequest(context);
        Response response = RestletUtils.getResponse(context);

        Serializable sessionId = session.getId();
        storeSessionId(sessionId, request, response);
    }

    @Override
    public Serializable getSessionId(SessionKey key) {
        Serializable id = super.getSessionId(key);
        if (id == null && RestletUtils.isRestlet(key)) {
            Request request = RestletUtils.getRequest(key);
            Response response = RestletUtils.getResponse(key);
            id = getSessionId(request, response);
        }
        return id;
    }

    protected Serializable getSessionId(Request request, Response response) {
        return getSessionIdCookieValue(request, response);
    }

    @Override
    protected void onExpiration(Session s, ExpiredSessionException ese, SessionKey key) {
        super.onExpiration(s, ese, key);
        onInvalidation(key);
    }

    @Override
    protected void onInvalidation(Session session, InvalidSessionException ise, SessionKey key) {
        super.onInvalidation(session, ise, key);
        onInvalidation(key);
    }

    private void onInvalidation(SessionKey key) {
        Request request = RestletUtils.getRequest(key);
        if (request != null) {
            request.getAttributes().remove(ShiroHttpServletRequest.REFERENCED_SESSION_ID_IS_VALID);
        }
    }

    @Override
    protected void onStop(Session session, SessionKey key) {
        super.onStop(session, key);
    }

    private void storeSessionId(Serializable currentId, Request request, Response response) {
        if (currentId == null) {
            String msg = "sessionId cannot be null when persisting for subsequent requests.";
            throw new IllegalArgumentException(msg);
        }

        CookieSetting cookie = createCookie();
        String idString = currentId.toString();
        cookie.setValue(idString);

        response.getCookieSettings().add(cookie);
    }

    private String getSessionIdCookieValue(Request request, Response response) {
        if (!(request instanceof Request)) {
            logger.debug("Current request is not an RestletRequest - cannot get session ID cookie.  Returning null.");
            return null;
        }
        if (request.getCookies().size() == 0) {
            return null;
        }
        org.restlet.data.Cookie sessionCookie = request.getCookies().getFirst(ShiroHttpSession.DEFAULT_SESSION_ID_NAME);
        return sessionCookie != null ? sessionCookie.getValue() : null;
    }

    private CookieSetting createCookie() {
        CookieSetting cookieSetting = new CookieSetting(ShiroHttpSession.DEFAULT_SESSION_ID_NAME, null);
        cookieSetting.setAccessRestricted(true);
        cookieSetting.setPath("/");
        cookieSetting.setComment("Skysail cookie-based authentication");
        cookieSetting.setMaxAge(300);
        return cookieSetting;
    }


}
