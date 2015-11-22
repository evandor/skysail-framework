//package de.twenty11.skysail.server.mgt.requests;
//
//import java.util.Date;
//import java.util.List;
//import java.util.Set;
//
//import org.restlet.data.CacheDirective;
//import org.restlet.data.CookieSetting;
//import org.restlet.data.Dimension;
//import org.restlet.data.Method;
//import org.restlet.data.Reference;
//import org.restlet.data.ServerInfo;
//import org.restlet.data.Status;
//import org.restlet.data.Warning;
//import org.restlet.util.Series;
//
//import de.twenty11.skysail.server.core.restlet.response.ResponseDescriptor;
//
//public class ResponseDetails {
//
//    private int age;
//    private Set<Method> allowedMethods;
//    //private ConcurrentMap<String, Object> attributes;
//    private List<CacheDirective> cacheDirectives;
//    private Series<CookieSetting> cookieSettings;
//    private Date date;
//    private Set<Dimension> dimensions;
//    private Reference locationRef;
//    private ServerInfo serverInfo;
//    private Status status;
//    private List<Warning> warnings;
//
//    public ResponseDetails(ResponseDescriptor rd) {
//        age = rd.getAge();
//        allowedMethods = rd.getAllowedMethods();
//       // attributes = rd.getAttributes();
//        cacheDirectives = rd.getCacheDirectives();
//        cookieSettings = rd.getCookieSettings();
//        date = rd.getDate();
//        dimensions = rd.getDimensions();
//        locationRef = rd.getLocationRef();
//        serverInfo = rd.getServerInfo();
//        status = rd.getStatus();
//        warnings = rd.getWarnings();
//    }
//
//    public int getAge() {
//        return age;
//    }
//
//    public Set<Method> getAllowedMethods() {
//        return allowedMethods;
//    }
//
////    public ConcurrentMap<String, Object> getAttributes() {
////        return attributes;
////    }
//
//    public List<CacheDirective> getCacheDirectives() {
//        return cacheDirectives;
//    }
//
//    public Series<CookieSetting> getCookieSettings() {
//        return cookieSettings;
//    }
//
//    public Date getDate() {
//        return date;
//    }
//
//    public Set<Dimension> getDimensions() {
//        return dimensions;
//    }
//
//    public Reference getLocationRef() {
//        return locationRef;
//    }
//
//    public ServerInfo getServerInfo() {
//        return serverInfo;
//    }
//
//    public Status getStatus() {
//        return status;
//    }
//
//    public List<Warning> getWarnings() {
//        return warnings;
//    }
//}
