package io.skysail.server.utils;

import io.skysail.api.domain.Identifiable;

import java.util.*;

import lombok.NonNull;

import org.restlet.data.Reference;

import de.twenty11.skysail.server.core.restlet.RouteBuilder;

public class PathUtils {

    public static Map<String, String> getSubstitutions(@NonNull Object object, @NonNull Map<String, Object> requestAttributes, List<RouteBuilder> routeBuilders) {
        Map<String, String> result = new HashMap<>();
        result.put("id", "UNKNOWN_ID");
        requestAttributes.entrySet().stream().forEach(entry -> {
            if (entry.getValue() instanceof String) {
                result.put(entry.getKey(), Reference.decode((String)entry.getValue()));
            }
        });
        routeBuilders.stream().forEach(routeBuilder -> {
            routeBuilder.getPathVariables().stream().forEach(pathVariable -> {
                if (requestAttributes.get(pathVariable) != null) {
                    result.put(pathVariable, (String)requestAttributes.get(pathVariable));
                }
            });
        });
        if (object instanceof Identifiable) {
            Identifiable identifiable = (Identifiable) object;
            if (identifiable.getId() != null) {
                result.put("id", identifiable.getId().replace("#", ""));
            }
        }
        if (object instanceof Map) {
            throw new IllegalStateException("logic not supported any more");
        }
        return result;
    }


}
