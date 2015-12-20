package io.skysail.server.utils;

import java.util.*;
import java.util.stream.Collectors;

import lombok.*;

import org.restlet.data.Reference;

import de.twenty11.skysail.server.core.restlet.RouteBuilder;
import io.skysail.domain.Identifiable;

public class PathUtils {

    private Map<String, Object> requestAttributes;
    private List<RouteBuilder> routeBuilders;
    @Getter
    private String idVariable = "id";

    public PathUtils(Map<String, Object> requestAttributes, List<RouteBuilder> routeBuilders) {
        this.requestAttributes = requestAttributes;
        this.routeBuilders = routeBuilders;
    }

    @Deprecated
    public static Map<String, String> getSubstitutions(@NonNull Object object, @NonNull Map<String, Object> requestAttributes, List<RouteBuilder> routeBuilders) {
        Map<String, String> result = new HashMap<>();
        requestAttributes.entrySet().stream().forEach(entry -> {
            if (entry.getValue() instanceof String) {
                result.put(entry.getKey(), Reference.decode((String)entry.getValue()));
            }
        });
        List<String> pathVariables = routeBuilders.stream().map(builder -> builder.getPathVariables()).flatMap(pv -> pv.stream()).collect(Collectors.toList());
        for (String pathVariable : pathVariables) {
            if (requestAttributes.get(pathVariable) != null) {
                result.put(pathVariable, (String)requestAttributes.get(pathVariable));
                pathVariables.remove(pathVariable);
            }
        }

        if (object instanceof Identifiable) {
            Identifiable identifiable = (Identifiable) object;
            if (identifiable.getId() != null) {
                if (pathVariables.size() == 1) {
                    result.put(pathVariables.get(0), identifiable.getId().replace("#", ""));
                } else {
                    result.put("id", identifiable.getId().replace("#", ""));
                }
            }
        }
        if (object instanceof Map) {
            throw new IllegalStateException("logic not supported any more");
        }
        return result;
    }

    public Map<String, String> getSubstitutions(Object object) {
        Map<String, String> result = new HashMap<>();
        requestAttributes.entrySet().stream().forEach(entry -> {
            if (entry.getValue() instanceof String) {
                result.put(entry.getKey(), Reference.decode((String)entry.getValue()));
            }
        });
        List<String> pathVariables = routeBuilders.stream().map(builder -> builder.getPathVariables()).flatMap(pv -> pv.stream()).collect(Collectors.toList());

        pathVariables.removeIf(pathVariable -> {
            if (requestAttributes.get(pathVariable) != null) {
                result.put(pathVariable, (String)requestAttributes.get(pathVariable));
                return true;
            }
            return false;
        });

//        for (String pathVariable : pathVariables) {
//            if (requestAttributes.get(pathVariable) != null) {
//                result.put(pathVariable, (String)requestAttributes.get(pathVariable));
//                pathVariables.remove(pathVariable);
//            }
//        }

        if (object instanceof Identifiable) {
            Identifiable identifiable = (Identifiable) object;
            if (identifiable.getId() != null) {
                if (pathVariables.size() == 1) {
                    idVariable = pathVariables.get(0);
                    result.put(idVariable, identifiable.getId().replace("#", ""));
                } else {
                    result.put("id", identifiable.getId().replace("#", ""));
                }
            }
        }
        return result;
    }


}
