package io.skysail.server.restlet.sourceconverter;

import io.skysail.server.model.ResourceModel;
import io.skysail.server.restlet.resources.SkysailServerResource;
import io.skysail.server.utils.OrientDbUtils;

import java.lang.reflect.Method;
import java.text.DateFormat;
import java.util.*;
import java.util.stream.Collectors;

import org.restlet.data.MediaType;
import org.restlet.representation.Variant;

public class ListSourceHtmlConverter extends SourceHtmlConverter {

    public ListSourceHtmlConverter(Object source, Variant target) {
        super(source, target);
    }

    protected Optional<Method> findHandlerMethod(Method[] methods) {
        Optional<Method> getHandlerMethod = Arrays.stream(methods).filter(m -> m.getName().contains("getHandler"))
                .findFirst();
        return getHandlerMethod;
    }
    
    protected SortedMap<String, Object> mapSource2(Object source, Map<String, Object> map, SkysailServerResource<?> resource) {
        SortedMap<String, Object> objectMap = new TreeMap<>(getComparator(resource));
        Map<String, Object> collect = map.entrySet().stream().filter(entry -> {
            return !entry.getKey().startsWith("@") || entry.getKey().equals("@rid");
        }).collect(Collectors.toMap(p -> p.getKey(), p -> p.getValue() == null ? "&lt;null&gt;" : p.getValue()));
        objectMap.putAll(collect);
        return objectMap;
    }
    
    public boolean isCompatible() {
        return source instanceof List && target.getMediaType().equals(MediaType.TEXT_HTML);
    }

    public List<Map<String, Object>> convert(ResourceModel<SkysailServerResource<?>,?> resourceModel) {
        
        if (!isCompatible()) {
            return null;//(Map<String, Object>)source;
        }

        mapper.setDateFormat(DateFormat.getDateInstance(DateFormat.LONG, determineLocale(resourceModel.getResource())));

        List<Map<String, Object>> result = new ArrayList<>();
        for (Object object : (List<?>) source) {

            if (object instanceof String && ((String) object).length() > 0
                    && ((String) object).substring(0, 1).equals("{")) {
//                Map<String, Object> mapFromJson = treatAsJson((String) object, fields, resource);
//                if (mapFromJson != null) {
//                    result.add(mapFromJson);
//                }
                //continue;
                throw new IllegalStateException("didnt expect to still get here");
            }

            if (object instanceof String) {
               result.add(new HashMap<String,Object>() {{
                   put("name", object);
               }});
               continue;
            }
            if (object instanceof Map) {
                //result.add(object);
                //continue;
                throw new IllegalStateException("didnt expect to still get here");
            }
            if (object.getClass().isEnum()) {
                //result.add(object.toString());
                throw new IllegalStateException("didnt expect to still get here");
                //continue;
            }
            if (!object.getClass().getName().contains("$$")) {
                @SuppressWarnings("unchecked")
                Map<String, Object> props = mapper.convertValue(object, Map.class);
                //resourceModel.getFields().stream().forEach(f -> check(f, props, resource));
                Map<String, Object> props2  = resourceModel.dataFromMap(props);
                result.add(props2);
                continue;
            }
            Map<String, Object> map = OrientDbUtils.toMap(object);
            if (map != null) {
                result.add(mapSource2(object, map, resourceModel.getResource()));
                throw new IllegalStateException("didnt expect to still get here");
            }
        }
        return result;
    }

}
