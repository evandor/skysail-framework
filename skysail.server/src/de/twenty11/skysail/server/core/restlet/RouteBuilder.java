package de.twenty11.skysail.server.core.restlet;

import io.skysail.server.app.ApiVersion;

import java.util.*;
import java.util.regex.*;

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
    private List<String> pathVariables = new ArrayList<>();

    private Pattern pathVariablesPattern = Pattern.compile("\\{([^\\}])*\\}");

    public RouteBuilder(String pathTemplate, Class<? extends ServerResource> targetClass) {
        Validate.notNull(pathTemplate, "pathTemplate may not be null");
        Validate.notNull(targetClass, "targetClass may not be null");
        this.pathTemplate = pathTemplate;
        this.targetClass = targetClass;
        pathVariables = extractPathVariables(pathTemplate);
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

    public String getPathTemplate(ApiVersion apiVersion) {
        if (apiVersion == null) {
            return pathTemplate;
        }
        return apiVersion.getVersionPath() + pathTemplate;
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

    public List<String> getPathVariables() {
        return pathVariables;
    }

    private List<String> extractPathVariables(String input) {
        List<String> result = new ArrayList<>();
        Matcher m = pathVariablesPattern.matcher(input);
        while (m.find()) {
            result.add(m.group(0).replace("}", "").replace("{", ""));
        }
        return result;
    }


}
