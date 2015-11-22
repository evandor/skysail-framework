//package de.twenty11.skysail.server.mgt.requests;
//
//import io.skysail.api.domain.Identifiable;
//
//import java.util.*;
//import java.util.concurrent.ConcurrentMap;
//
//import lombok.*;
//
//import org.restlet.data.*;
//import org.restlet.util.Series;
//
//@Getter
//@Setter
//public class RequestDetails implements Identifiable {
//
//    private String id;
//
//    private Long requestId;
//
//    private ConcurrentMap<String, Object> attributes;
//
//    private List<CacheDirective> cacheDirectives;
//
//    private ClientInfo clientInfo;
//
//    private Conditions conditions;
//
//    private Series<Cookie> cookies;
//
//    private Date date;
//
//    private Reference hostRef;
//
//    private Method method;
//
//    private Reference originalRef;
//
//    private Protocol protocol;
//
//    private List<Range> ranges;
//
//    private Reference referrerRef;
//
//    private Reference resourceRef;
//
//    private Reference rootRef;
//
//    private List<Warning> warnings;
//
//	private Series<?> headers;
//
//    public RequestDetails(RequestDescriptor rd) {
//        requestId = rd.getRequestId();
//        attributes = rd.getAttributes();
//        headers = (Series<?>) rd.getAttributes().get("org.restlet.http.headers");
//        cacheDirectives = rd.getCacheDirectives();
//        clientInfo = rd.getClientInfo();
//        conditions = rd.getConditions();
//        cookies = rd.getCookies();
//        date = rd.getDate();
//        hostRef = rd.getHostRef();
//        method = rd.getMethod();
//        originalRef = rd.getOriginalRef();
//        protocol = rd.getProtocol();
//        ranges = rd.getRanges();
//        referrerRef = rd.getReferrerRef();
//        resourceRef = rd.getResourceRef();
//        rootRef = rd.getRootRef();
//        warnings = rd.getWarnings();
//    }
//
//    public String getReferrerRef() {
//        return referrerRef != null ? referrerRef.toString() : "";
//    }
//
//    public String getResourceRef() {
//        return resourceRef.toString();
//    }
//
//    public String getRootRef() {
//        return rootRef.toString();
//    }
//
//
//    public String getHostRef() {
//        return hostRef.toString();
//    }
//
//    public Method getMethod() {
//        return method;
//    }
//
//    public String getOriginalRef() {
//        return originalRef.toString();
//    }
//
//    public Protocol getProtocol() {
//        return protocol;
//    }
//
//    public Series<?> getHeaders() {
//	    return headers;
//    }
//
//    public void setHeaders(Series<?> headers) {
//	    this.headers = headers;
//    }
//}
