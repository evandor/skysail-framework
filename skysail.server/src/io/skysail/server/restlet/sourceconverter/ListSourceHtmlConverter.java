package io.skysail.server.restlet.sourceconverter;

import io.skysail.api.forms.Postfix;
import io.skysail.api.forms.Prefix;
import io.skysail.api.links.Link;
import io.skysail.server.forms.ListView;
import io.skysail.server.restlet.resources.SkysailServerResource;
import io.skysail.server.utils.OrientDbUtils;

import java.io.IOException;
import java.lang.reflect.Field;
import java.net.URL;
import java.text.DateFormat;
import java.util.ArrayList;
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

import aQute.bnd.annotation.component.Component;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;

import de.twenty11.skysail.server.app.AbstractSourceConverter;

@Component
@Slf4j
public class ListSourceHtmlConverter extends AbstractSourceConverter implements SourceConverter {

    private volatile ObjectMapper mapper = new ObjectMapper();
    private String indexPageName;

    public ListSourceHtmlConverter() {
        mapper.configure(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS, false);
        // mapper.configure(SerializationFeature.FAIL_ON_EMPTY_BEANS, false);
    }

    @Override
    public boolean isCompatible() {
        return getSource() instanceof List && getTarget().getMediaType().equals(MediaType.TEXT_HTML);
    }

    @Override
    public Object convert(SkysailServerResource<?> resource, List<Field> fields, String indexPageName) {

        this.indexPageName = indexPageName;
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
            if (object instanceof Map) {
                result.add(object);
                continue;
            }
            if (object.getClass().isEnum()) {
                result.add(object.toString());
                continue;
            }
            if (!object.getClass().getName().contains("$$")) {
                @SuppressWarnings("unchecked")
                Map<String, Object> props = mapper.convertValue(object, Map.class);
                fields.stream().forEach(f -> check(f, props, resource));
                result.add(props);
                continue;
            }
            Map<String, Object> map = OrientDbUtils.toMap(object);
            if (map != null) {
                result.add(mapSource2(object, map, resource));
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
        ListView listViewAnnotation = f.getAnnotation(io.skysail.server.forms.ListView.class);
        if (listViewAnnotation == null) {
            return null;
        }
        String newValue = null;
        if (listViewAnnotation.truncate() > 3) {
            //if (f.getName() instanceof String) {
                String oldValue = newValue = (String) result.get(f.getName());
                if (oldValue != null && oldValue.length() > listViewAnnotation.truncate()) {
                    newValue = "<span title='" + oldValue + "'>"
                            + oldValue.substring(0, listViewAnnotation.truncate() - 3) + "...</span>";
                }
            //}
        }
        if (URL.class.equals(f.getType())) {
            newValue = "<a href='" + result.get(f.getName()).toString() + "' target=\"_blank\">" + newValue + "</a>";
        } else {
            Class<? extends SkysailServerResource<?>> linkedResource = listViewAnnotation.link();
            if (linkedResource != null) {
                List<Link> links = resource.getLinks();
                String id = result.get("id") != null ? result.get("id").toString().replace("#", "") : null;
                if (links != null && id != null) {
                    Optional<Link> findFirst = links.stream().filter(l -> {
                        return linkedResource.equals(l.getCls()) && id.equals(l.getRefId());
                    }).findFirst();
                    if (findFirst.isPresent()) {
                        if (indexPageName.equals("indexMobile")) {
                            newValue = "<a href='"+findFirst.get().getUri()+"'><input type='button' class='btn btn-primary btn-lg btn-block' value='" + newValue + "' /></a>";
                        } else {
                            newValue = "<a href='" + findFirst.get().getUri() + "'>" + newValue + "</a>";                            
                        }
                    }
                }
            }
        }
        Prefix prefix = f.getAnnotation(Prefix.class);
        if (newValue != null && prefix != null) {
            newValue = result.get(prefix.methodName()) + "&nbsp;" + newValue;
        }
        Postfix postfix = f.getAnnotation(Postfix.class);
        if (newValue != null && postfix != null) {
            newValue = newValue + "&nbsp;" + result.get(postfix.methodName());
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
