package de.twenty11.skysail.server.mgt.requests;

import java.util.Date;
import java.util.List;
import java.util.concurrent.ConcurrentMap;

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

import de.twenty11.skysail.server.core.restlet.request.RequestDescriptor;

public class RequestDetails {

    private Long requestId;

    private ConcurrentMap<String, Object> attributes;

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

	private Series<?> headers;

    public RequestDetails(RequestDescriptor rd) {
        requestId = rd.getRequestId();
        attributes = rd.getAttributes();
        headers = (Series<?>) rd.getAttributes().get("org.restlet.http.headers");
        cacheDirectives = rd.getCacheDirectives();
        clientInfo = rd.getClientInfo();
        conditions = rd.getConditions();
        cookies = rd.getCookies();
        date = rd.getDate();
        hostRef = rd.getHostRef();
        method = rd.getMethod();
        originalRef = rd.getOriginalRef();
        protocol = rd.getProtocol();
        ranges = rd.getRanges();
        referrerRef = rd.getReferrerRef();
        resourceRef = rd.getResourceRef();
        rootRef = rd.getRootRef();
        warnings = rd.getWarnings();
    }

    public List<Range> getRanges() {
        return ranges;
    }

    public String getReferrerRef() {
        return referrerRef != null ? referrerRef.toString() : "";
    }

    public String getResourceRef() {
        return resourceRef.toString();
    }

    public String getRootRef() {
        return rootRef.toString();
    }

    public List<Warning> getWarnings() {
        return warnings;
    }

    public Long getRequestId() {
        return requestId;
    }

    public ConcurrentMap<String, Object> getAttributes() {
        return attributes;
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

    public String getHostRef() {
        return hostRef.toString();
    }

    public Method getMethod() {
        return method;
    }

    public String getOriginalRef() {
        return originalRef.toString();
    }

    public Protocol getProtocol() {
        return protocol;
    }

    public Series<?> getHeaders() {
	    return headers;
    }

    public void setHeaders(Series<?> headers) {
	    this.headers = headers;
    }
}
