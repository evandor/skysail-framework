package io.skysail.server.restlet.sourceconverter;

import io.skysail.server.model.ResourceModel;
import io.skysail.server.restlet.resources.SkysailServerResource;
import io.skysail.server.utils.OrientDbUtils;

import java.lang.reflect.Method;
import java.text.DateFormat;
import java.util.*;
import java.util.stream.Collectors;

import org.restlet.data.*;
import org.restlet.representation.Variant;

import com.fasterxml.jackson.databind.*;

public class ListSourceHtmlConverter {

    private volatile ObjectMapper mapper = new ObjectMapper();
    
    private Object source;
    private Variant target;
    
    public ListSourceHtmlConverter(Object source, Variant target) {
        mapper.configure(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS, false);
        this.source = source;
        this.target = target;
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

    private Locale determineLocale(SkysailServerResource<?> resource) {
        List<Preference<Language>> acceptedLanguages = resource.getRequest().getClientInfo().getAcceptedLanguages();
        Locale localeToUse = Locale.getDefault();
        if (!acceptedLanguages.isEmpty()) {
            String[] languageSplit = acceptedLanguages.get(0).getMetadata().getName().split("-");
            if (languageSplit.length == 1) {
                localeToUse = new Locale(languageSplit[0]);
            } else if (languageSplit.length == 2) {
                localeToUse = new Locale(languageSplit[0], languageSplit[1]);
            }
        }
        return localeToUse;
    }

    protected Comparator<String> getComparator(SkysailServerResource<?> resource) {
        return new Comparator<String>() {

            @Override
            public int compare(String o1, String o2) {
                List<String> fieldNames = resource.getFields();
                if (fieldNames.indexOf(o1) == -1 && fieldNames.indexOf(o2) == -1) {
                    return o1.compareTo(o2);
                }
                if (fieldNames.indexOf(o1) == -1) {
                    return -1;
                }
                if (fieldNames.indexOf(o2) == -1) {
                    return 1;
                }
                return fieldNames.indexOf(o1) - fieldNames.indexOf(o2);

            }

        };
    }

}
