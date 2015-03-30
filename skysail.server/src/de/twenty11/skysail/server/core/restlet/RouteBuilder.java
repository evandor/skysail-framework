package de.twenty11.skysail.server.core.restlet;

import org.apache.commons.lang.Validate;
import org.restlet.Restlet;
import org.restlet.resource.ServerResource;

import com.google.common.base.Predicate;

public class RouteBuilder {

    private final String pathTemplate;
    private Class<? extends ServerResource> targetClass;
    private Restlet restlet;

	private boolean needsAuthentication = true; // default
	private Predicate<String[]> rolesForAuthorization;

    public RouteBuilder(String pathTemplate, Class<? extends ServerResource> targetClass) {
        Validate.notNull(pathTemplate, "pathTemplate may not be null");
        Validate.notNull(targetClass, "targetClass may not be null");
        this.pathTemplate = pathTemplate;
        this.targetClass = targetClass;
    }

    public RouteBuilder(String pathTemplate, Restlet restlet) {
        Validate.notNull(pathTemplate, "pathTemplate may not be null");
        Validate.notNull(restlet, "target may not be null");
        this.pathTemplate = pathTemplate;
        this.restlet = restlet;
    }

    public RouteBuilder setText(String text) {
        return this;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append(pathTemplate).append(" -> ").append(targetClass);
        sb.append(")");
        return sb.toString();
    }

    public Class<? extends ServerResource> getTargetClass() {
        return targetClass;
    }

    public Restlet getRestlet() {
        return restlet;
    }

    public String getPathTemplate() {
        return pathTemplate;
    }
    
    /**
     * the current user needs on of those roles to get authorized
     */
    public RouteBuilder authorizeWith(Predicate<String[]> predicate) {
        this.rolesForAuthorization = predicate;
        return this;
    }

    public Predicate<String[]> getRolesForAuthorization() {
        return rolesForAuthorization;
    }

    public boolean needsAuthentication() {
        return needsAuthentication;
    }
    
    public RouteBuilder noAuthenticationNeeded() {
		this.needsAuthentication = false;
		return this;
	}

}
