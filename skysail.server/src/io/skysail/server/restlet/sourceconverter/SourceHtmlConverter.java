package io.skysail.server.restlet.sourceconverter;

import io.skysail.server.restlet.resources.SkysailServerResource;

import java.lang.reflect.Method;
import java.util.*;
import java.util.stream.Collectors;

import org.restlet.data.*;

import com.fasterxml.jackson.databind.*;

public class SourceHtmlConverter {

    protected volatile ObjectMapper mapper = new ObjectMapper();
    
    protected Object source;

    public SourceHtmlConverter(Object source) {
        mapper.configure(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS, false);
        this.source = source;
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

    protected Locale determineLocale(SkysailServerResource<?> resource) {
        if (resource.getRequest() == null) {
            return Locale.getDefault();
        }
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
