package de.twenty11.skysail.server.app.sourceconverter;

import io.skysail.api.forms.ListView;
import io.skysail.api.forms.Prefix;
import io.skysail.server.restlet.resources.SkysailServerResource;

import java.io.IOException;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.net.URL;
import java.text.DateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Optional;

import lombok.extern.slf4j.Slf4j;

import org.restlet.data.Language;
import org.restlet.data.MediaType;
import org.restlet.data.Preference;
import org.restlet.data.Reference;

import aQute.bnd.annotation.component.Component;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;

import de.twenty11.skysail.server.app.AbstractSourceConverter;

@Component
@Slf4j
public class ListSourceHtmlConverter extends AbstractSourceConverter implements SourceConverter {

    private static final int MAX_LENGTH_FOR_TRUNCATED_FIELDS = 20;
    private ObjectMapper mapper = new ObjectMapper();

    public ListSourceHtmlConverter() {
        mapper.configure(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS, false);
    }

    @Override
    public boolean isCompatible() {
        return getSource() instanceof List && getTarget().getMediaType().equals(MediaType.TEXT_HTML);
    }

    @Override
    public Object convert(SkysailServerResource<?> resource, List<Field> fields) {

        mapper.setDateFormat(DateFormat.getDateInstance(DateFormat.LONG, determineLocale(resource)));

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
        io.skysail.api.forms.Field fieldAnnotation = f.getAnnotation(io.skysail.api.forms.Field.class);
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
            if (URL.class.equals(f.getType())) {
                newValue = "<a href='" + result.get(f.getName()).toString() +"' target=\"_blank\">"
                        + newValue + "</a>";
            } else {
                Reference originalRef = resource.getRequest().getOriginalRef();
                Object id = result.get("id") != null ? result.get("id") : "wo";
                newValue = "<a href='" + originalRef.toString() + "/" + ((String) id).replace("#", "") + "/'>"
                        + newValue + "</a>";
            }
        }
        Prefix prefix = f.getAnnotation(Prefix.class);
        if (newValue != null && prefix != null) {

            newValue = result.get(prefix.methodName()) + "&nbsp;" + newValue;
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
