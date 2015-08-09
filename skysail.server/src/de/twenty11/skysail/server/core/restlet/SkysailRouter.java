package de.twenty11.skysail.server.core.restlet;

import io.skysail.server.app.ApiVersion;
import io.skysail.server.restlet.resources.ListServerResource;

import java.util.*;
import java.util.Map.Entry;
import java.util.concurrent.ConcurrentHashMap;

import lombok.extern.slf4j.Slf4j;

import org.restlet.Context;
import org.restlet.resource.ServerResource;
import org.restlet.routing.*;
import org.restlet.security.Authorizer;

import com.google.common.base.Predicate;

import de.twenty11.skysail.server.security.AuthenticatedAuthorizer;

@Slf4j
public class SkysailRouter extends Router {

    private Map<String, RouteBuilder> pathRouteBuilderMap = new ConcurrentHashMap<String, RouteBuilder>();

    private Predicate<String[]> defaultRolesPredicate;

    private ApiVersion apiVersion;

    public SkysailRouter(Context context) {
        super(context);
    }

    public TemplateRoute attach(String pathTemplate, Class<? extends ServerResource> targetClass) {
        log.warn("please use a RouteBuilder to attach this resource: {}", targetClass);
        return attach(pathTemplate, createFinder(targetClass));
    }

    public void attach(RouteBuilder routeBuilder) {

        String pathTemplate = routeBuilder.getPathTemplate(apiVersion);
        pathRouteBuilderMap.put(pathTemplate, routeBuilder);
        if (routeBuilder.getTargetClass() == null) {
            attachForTargetClassNull(routeBuilder);
            return;
        }

        if (ListServerResource.class.isAssignableFrom(routeBuilder.getTargetClass())) {
            String metadataPath = pathTemplate + "!meta";
            RouteBuilder metaRouteBuilder = new RouteBuilder(metadataPath, routeBuilder.getTargetClass());
            log.info("routing path '{}' -> '{}' -> '{}'", metadataPath, "RolesPredicateAuthorizer",
                    metaRouteBuilder.getTargetClass().getName());
            attach(metadataPath, createIsAuthenticatedAuthorizer(metaRouteBuilder));
        }

        if (!routeBuilder.needsAuthentication()) {
            attachForNoAuthenticationNeeded(routeBuilder);
            return;
        }
        Authorizer isAuthenticatedAuthorizer = createIsAuthenticatedAuthorizer(routeBuilder);
        log.info("routing path '{}' -> '{}' -> '{}'", pathTemplate, "RolesPredicateAuthorizer", routeBuilder
                .getTargetClass().getName());
        attach(pathTemplate, isAuthenticatedAuthorizer);
    }

    public void detachAll() {
        getRoutes().clear();
    }

    public RouteBuilder getRouteBuilder(String pathTemplate) {
        return pathRouteBuilderMap.get(pathTemplate);
    }

    public Map<String, RouteBuilder> getRouteBuilders() {
        return Collections.unmodifiableMap(pathRouteBuilderMap);
    }

    /**
     * provides, for a given skysail server resource, the path templates the
     * resource was attached to.
     *
     * @param cls
     * @return List of path templates
     */
    // TODO maybe use getRouteBuildersForResource instead?
    public List<String> getTemplatePathForResource(Class<? extends ServerResource> cls) {
        List<String> result = new ArrayList<>();
        for (Entry<String, RouteBuilder> entries : pathRouteBuilderMap.entrySet()) {
            if (entries.getValue() == null || entries.getValue().getTargetClass() == null) {
                continue;
            }
            if (entries.getValue().getTargetClass().equals(cls)) {
                result.add(entries.getKey());
            }
        }
        return result;
    }

    public List<RouteBuilder> getRouteBuildersForResource(Class<? extends ServerResource> cls) {
        List<RouteBuilder> result = new ArrayList<>();
        for (Entry<String, RouteBuilder> entries : pathRouteBuilderMap.entrySet()) {
            if (entries.getValue() == null || entries.getValue().getTargetClass() == null) {
                continue;
            }
            if (entries.getValue().getTargetClass().equals(cls)) {
                result.add(entries.getValue());
            }
        }
        return result;
    }

    public Map<String, RouteBuilder> getRoutesMap() {
        return Collections.unmodifiableMap(pathRouteBuilderMap);
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        if (pathRouteBuilderMap != null) {
            for (String key : pathRouteBuilderMap.keySet()) {
                sb.append(key).append(": ").append(pathRouteBuilderMap.get(key)).append("\n");
            }
        }
        return sb.toString();
    }

    public void setAuthorizationDefaults(Predicate<String[]> predicate) {
        this.defaultRolesPredicate = predicate;

    }

    private Authorizer createIsAuthenticatedAuthorizer(RouteBuilder routeBuilder) {
        Predicate<String[]> predicateToUse = routeBuilder.getRolesForAuthorization() == null ? defaultRolesPredicate
                : routeBuilder.getRolesForAuthorization();
        routeBuilder.authorizeWith(predicateToUse);

        RolesPredicateAuthorizer authorizer = new RolesPredicateAuthorizer(predicateToUse);
        authorizer.setContext(getContext());
        authorizer.setNext(routeBuilder.getTargetClass());
        Authorizer isAuthenticatedAuthorizer = new AuthenticatedAuthorizer();
        isAuthenticatedAuthorizer.setNext(authorizer);
        return isAuthenticatedAuthorizer;
    }

    private void attachForNoAuthenticationNeeded(RouteBuilder routeBuilder) {
        log.info("routing path '{}' -> '{}'", routeBuilder.getPathTemplate(apiVersion), routeBuilder.getTargetClass()
                .getName());
        attach(routeBuilder.getPathTemplate(apiVersion), routeBuilder.getTargetClass());
    }

    private void attachForTargetClassNull(RouteBuilder routeBuilder) {
        if (routeBuilder.getRestlet() == null) {
            throw new IllegalStateException("RouteBuilder with neither TargetClass nor Restlet defined!");
        }
        log.info("routing path '{}' -> Restlet '{}'", routeBuilder.getPathTemplate(apiVersion), routeBuilder
                .getRestlet().getClass().getSimpleName());
        attach(routeBuilder.getPathTemplate(apiVersion), routeBuilder.getRestlet());
    }

    public void setApiVersion(ApiVersion apiVersion) {
        this.apiVersion = apiVersion;
    }

}
