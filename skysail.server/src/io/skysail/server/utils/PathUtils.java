package io.skysail.server.utils;

import io.skysail.api.domain.Identifiable;
import io.skysail.server.restlet.resources.SkysailServerResource;

import java.util.*;

import lombok.NonNull;

import org.restlet.data.Reference;

public class PathUtils {

    public static Map<String, String> getSubstitutions(@NonNull Object object, @NonNull SkysailServerResource<?> resource) {
        Map<String, String> result = new HashMap<>();
        result.put("id", "UNKNOWN_ID");
        resource.getRequestAttributes().entrySet().stream().forEach(entry -> {
            if (entry.getValue() instanceof String) {
                result.put(entry.getKey(), Reference.decode((String)entry.getValue()));
            }
        });
        if (object instanceof Identifiable) {
            Identifiable identifiable = (Identifiable) object;
            if (identifiable.getId() != null) {
                result.put("id", identifiable.getId().replace("#", ""));
            }
        }
        if (object instanceof Map) {
            throw new IllegalStateException("logic not supported any more");
//            Map<String, Object> map = (Map) object;
//            if (map.get("@rid") != null) {
//                result.put("id",map.get("@rid").toString().replace("#", ""));
//            }
//            if (map.get("id") != null) {
//                result.put("id", map.get("id").toString().replace("#", ""));
//            }
        }
        return result;
    }


}
