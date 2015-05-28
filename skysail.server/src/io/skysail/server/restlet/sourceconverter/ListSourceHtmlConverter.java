package io.skysail.server.restlet.sourceconverter;

import io.skysail.server.model.ResourceModel;
import io.skysail.server.restlet.resources.SkysailServerResource;
import io.skysail.server.utils.OrientDbUtils;

import java.text.DateFormat;
import java.util.*;

import lombok.extern.slf4j.Slf4j;

import org.restlet.data.*;

import aQute.bnd.annotation.component.Component;

import com.fasterxml.jackson.databind.*;

import de.twenty11.skysail.server.app.AbstractSourceConverter;

@Component
@Slf4j
public class ListSourceHtmlConverter extends AbstractSourceConverter implements SourceConverter {

    private volatile ObjectMapper mapper = new ObjectMapper();
    private String indexPageName;

    public ListSourceHtmlConverter() {
        mapper.configure(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS, false);
    }

    @Override
    public boolean isCompatible() {
        return getSource() instanceof List && getTarget().getMediaType().equals(MediaType.TEXT_HTML);
    }

    @Override
    public Object convert(SkysailServerResource<?> resource, ResourceModel<SkysailServerResource<?>,?> resourceModel,  String indexPageName) {

        this.indexPageName = indexPageName;
        mapper.setDateFormat(DateFormat.getDateInstance(DateFormat.LONG, determineLocale(resource)));

        List<Object> result = new ArrayList<Object>();
        for (Object object : (List<?>) getSource()) {

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
                //resourceModel.getFields().stream().forEach(f -> check(f, props, resource));
                Map<String, Object> props2  = resourceModel.dataFromMap(props);
                result.add(props2);
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
