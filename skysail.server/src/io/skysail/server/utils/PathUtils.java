package io.skysail.server.utils;

import io.skysail.api.domain.Identifiable;

import java.util.*;
import java.util.stream.Collectors;

import lombok.*;

import org.restlet.data.Reference;

import de.twenty11.skysail.server.core.restlet.RouteBuilder;

/**
 * Initialized with the request attributes list and the list of routeBuilders, this utility
 * class will help you determine a map of substitutions for the url placeholders for a given
 * object.
 *
 * For example, lets say we have a resource mounted at /v2/Lists/{id}/Todos/{todoId}.
 *
 */
public class PathUtils {

    /** e.g. id=#15:5, listId=#16:3, headers=[...] */
    final private Map<String, Object> requestAttributes;

    final private List<RouteBuilder> routeBuilders;

    @Getter
    private String idVariable = "id";

    public PathUtils(@NonNull Map<String, Object> requestAttributes, @NonNull List<RouteBuilder> routeBuilders) {
        this.requestAttributes = requestAttributes;
        this.routeBuilders = routeBuilders;
    }

    /**
     * Tries to determine the path substitutions from
     *
     * 1.) the request attributes: each string value is put on the list
     * 2.) the route builders path variables:
     * 3.) the objects id, if existent
     *
     */
    public Map<String, String> getSubstitutions(Object object) {
        Map<String, String> result = new HashMap<>();
        requestAttributes.entrySet().stream().forEach(entry -> {
            if (entry.getValue() instanceof String) {
                result.put(entry.getKey(), sanitize(Reference.decode((String)entry.getValue())));
            }
        });

        List<String> pathVariables = routeBuilders.stream().map(builder -> builder.getPathVariables()).flatMap(pv -> pv.stream()).collect(Collectors.toList());
        pathVariables.removeIf(pathVariable -> {
            if (requestAttributes.get(pathVariable) != null) {
                result.put(pathVariable, sanitize((String)requestAttributes.get(pathVariable)));
                return true;
            }
            return false;
        });

        if (object instanceof Identifiable) {
            Identifiable identifiable = (Identifiable) object;
            if (identifiable.getId() != null) {
                if (pathVariables.size() == 1) {
                    idVariable = pathVariables.get(0);
                    result.put(idVariable, sanitize(identifiable.getId()));
                } else {
                    result.put("id", sanitize(identifiable.getId()));
                }
            }
        }
        return result;
    }

    private String sanitize(String id) {
        return id.replace("#", "");
    }


}
