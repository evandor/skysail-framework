package de.twenty11.skysail.server.app.sourceconverter;

import java.io.IOException;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.text.DateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import lombok.extern.slf4j.Slf4j;

import org.restlet.data.MediaType;
import org.restlet.data.Reference;

import aQute.bnd.annotation.component.Component;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;

import de.twenty11.skysail.api.forms.ListView;
import de.twenty11.skysail.server.app.AbstractSourceConverter;
import de.twenty11.skysail.server.core.restlet.SkysailServerResource;

@Component
@Slf4j
public class ListSourceHtmlConverter extends AbstractSourceConverter implements SourceConverter {

    private static final int MAX_LENGTH_FOR_TRUNCATED_FIELDS = 20;
    private ObjectMapper mapper = new ObjectMapper();

    public ListSourceHtmlConverter() {
        mapper.configure(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS, false);
        mapper.setDateFormat(DateFormat.getDateInstance(DateFormat.SHORT));
    }

    @Override
    public boolean isCompatible() {
        return getSource() instanceof List && getTarget().getMediaType().equals(MediaType.TEXT_HTML);
    }

    @Override
    public Object convert(SkysailServerResource<?> resource, List<Field> fields) {
        List<Object> result = new ArrayList<Object>();
        for (Object object : (List<?>) getSource()) {

            if (object instanceof String && ((String) object).length() > 0
                    && ((String) object).substring(0, 1).equals("{")) {
                Map<String, Object> mapFromJson = treatAsJson((String) object, fields, resource);
                if (mapFromJson != null) {
                    result.add(mapFromJson);
                }
                continue;
            }

            if (object instanceof String) {
                result.add(object);
                continue;
            }
            if (!object.getClass().getName().contains("$$")) {
                @SuppressWarnings("unchecked")
                Map<String, Object> props = mapper.convertValue(object, Map.class);
                fields.stream().forEach(f -> check(f, props, resource));
                result.add(props);
                continue;
            }
            Method[] methods = object.getClass().getMethods();
            Optional<Method> getHandlerMethod = findHandlerMethod(methods);
            if (getHandlerMethod.isPresent()) {
                result.add(mapSource(object, getHandlerMethod, resource));
            }
        }
        return result;
    }

    private Map<String, Object> treatAsJson(String json, List<Field> fields, SkysailServerResource<?> resource) {
        try {
            Map<String, Object> result = mapper.readValue(json, new TypeReference<HashMap<String, Object>>() {
            });
            fields.stream().forEach(f -> check(f, result, resource));
            return result;
        } catch (IOException e) {
            log.error(e.getMessage(), e);
            return null;
        }
    }

    private Object check(Field f, Map<String, Object> result, SkysailServerResource<?> resource) {
        de.twenty11.skysail.api.forms.Field fieldAnnotation = f
                .getAnnotation(de.twenty11.skysail.api.forms.Field.class);
        if (fieldAnnotation == null) {
            return null;
        }
        String newValue = null;
        if (Arrays.asList(fieldAnnotation.listView()).contains(ListView.TRUNCATE)) {
            if (f.getName() instanceof String) {
                String oldValue = newValue = (String) result.get(f.getName());
                if (oldValue != null && oldValue.length() > MAX_LENGTH_FOR_TRUNCATED_FIELDS) {
                    newValue = oldValue.substring(0, MAX_LENGTH_FOR_TRUNCATED_FIELDS - 3) + "...";
                }
            }
        }
        if (Arrays.asList(fieldAnnotation.listView()).contains(ListView.LINK)) {
            Reference originalRef = resource.getRequest().getOriginalRef();
            newValue = "<a href='" + originalRef.toString() + "/" + ((String) result.get("id")).replace("#", "")
                    + "/'>" + newValue + "</a>";
        }
        if (newValue != null) {
            result.put(f.getName(), newValue);
        }
        return null;
    }

    @Override
    protected Comparator<String> getComparator(SkysailServerResource<?> resource) {
        return new Comparator<String>() {

            @Override
            public int compare(String o1, String o2) {
                List<String> fieldNames = resource.getFields();
                // if (!fieldNames.contains(o1)) {
                // fieldNames.add(o1);
                // }
                // if (!fieldNames.contains(o2)) {
                // fieldNames.add(o2);
                // }
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
