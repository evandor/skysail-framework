package de.twenty11.skysail.server.core.restlet.response;

import java.util.Date;
import java.util.List;
import java.util.Set;
import java.util.concurrent.ConcurrentMap;

import org.restlet.Response;
import org.restlet.data.AuthenticationInfo;
import org.restlet.data.CacheDirective;
import org.restlet.data.CookieSetting;
import org.restlet.data.Dimension;
import org.restlet.data.Method;
import org.restlet.data.Reference;
import org.restlet.data.ServerInfo;
import org.restlet.data.Status;
import org.restlet.data.Warning;
import org.restlet.util.Series;

public class ResponseDescriptor {

    private int age;
    private Set<Method> allowedMethods;
    private ConcurrentMap<String, Object> attributes;
    private AuthenticationInfo authenticationInfo;
    private List<CacheDirective> cacheDirectives;
    private Series<CookieSetting> cookieSettings;
    private Date date;
    private Set<Dimension> dimensions;
    private Reference locationRef;
    private Date retryAfter;
    private ServerInfo serverInfo;
    private Status status;
    private List<Warning> warnings;

    public ResponseDescriptor(Response response) {
        age = response.getAge();
        allowedMethods = response.getAllowedMethods();
        attributes = response.getAttributes();
        authenticationInfo = response.getAuthenticationInfo();
        cacheDirectives = response.getCacheDirectives();
        cookieSettings = response.getCookieSettings();
        date = response.getDate();
        dimensions = response.getDimensions();
        locationRef = response.getLocationRef();
        retryAfter = response.getRetryAfter();
        serverInfo = response.getServerInfo();
        status = response.getStatus();
        warnings = response.getWarnings();
    }

    public int getAge() {
        return age;
    };

    public ConcurrentMap<String, Object> getAttributes() {
        return attributes;
    }

    public AuthenticationInfo getAuthenticationInfo() {
        return authenticationInfo;
    }

    public List<CacheDirective> getCacheDirectives() {
        return cacheDirectives;
    }

    public Series<CookieSetting> getCookieSettings() {
        return cookieSettings;
    }

    public Date getDate() {
        return date;
    }

    public Set<Dimension> getDimensions() {
        return dimensions;
    }

    public Reference getLocationRef() {
        return locationRef;
    }

    public Date getRetryAfter() {
        return retryAfter;
    }

    public ServerInfo getServerInfo() {
        return serverInfo;
    }

    public Status getStatus() {
        return status;
    }

    public List<Warning> getWarnings() {
        return warnings;
    }

    public Set<Method> getAllowedMethods() {
        return allowedMethods;
    }
}
