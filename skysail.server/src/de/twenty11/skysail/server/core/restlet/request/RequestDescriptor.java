package de.twenty11.skysail.server.core.restlet.request;

import java.util.Date;
import java.util.List;
import java.util.concurrent.ConcurrentMap;

import org.restlet.Request;
import org.restlet.data.CacheDirective;
import org.restlet.data.ClientInfo;
import org.restlet.data.Conditions;
import org.restlet.data.Cookie;
import org.restlet.data.Method;
import org.restlet.data.Protocol;
import org.restlet.data.Range;
import org.restlet.data.Reference;
import org.restlet.data.Warning;
import org.restlet.util.Series;

public class RequestDescriptor {

    private ConcurrentMap<String, Object> attributes;
    private Long requestId;
    private List<CacheDirective> cacheDirectives;
    private ClientInfo clientInfo;
    private Conditions conditions;
    private Series<Cookie> cookies;
    private Date date;
    private Reference hostRef;
    private Method method;
    private Reference originalRef;
    private Protocol protocol;
    private List<Range> ranges;
    private Reference referrerRef;
    private Reference resourceRef;
    private Reference rootRef;
    private List<Warning> warnings;

    public RequestDescriptor(Long requestId, Request request) {

        this.requestId = requestId;
        this.attributes = request.getAttributes();
        this.cacheDirectives = request.getCacheDirectives();
        this.clientInfo = request.getClientInfo();
        conditions = request.getConditions();
        cookies = request.getCookies();
        date = request.getDate();
        hostRef = request.getHostRef();
        method = request.getMethod();
        originalRef = request.getOriginalRef();
        protocol = request.getProtocol();
        ranges = request.getRanges();
        referrerRef = request.getReferrerRef();
        resourceRef = request.getResourceRef();
        rootRef = request.getRootRef();
        warnings = request.getWarnings();
    }

    public List<Range> getRanges() {
        return ranges;
    }

    public Reference getReferrerRef() {
        return referrerRef;
    }

    public Reference getResourceRef() {
        return resourceRef;
    }

    public Reference getRootRef() {
        return rootRef;
    }

    public List<Warning> getWarnings() {
        return warnings;
    }

    public ConcurrentMap<String, Object> getAttributes() {
        return attributes;
    }

    public Long getRequestId() {
        return requestId;
    }

    public List<CacheDirective> getCacheDirectives() {
        return cacheDirectives;
    }

    public ClientInfo getClientInfo() {
        return clientInfo;
    }

    public Conditions getConditions() {
        return conditions;
    }

    public Series<Cookie> getCookies() {
        return cookies;
    }

    public Date getDate() {
        return date;
    }

    public Reference getHostRef() {
        return hostRef;
    }

    public Method getMethod() {
        return method;
    }

    public Reference getOriginalRef() {
        return originalRef;
    }

    public Protocol getProtocol() {
        return protocol;
    }
}
