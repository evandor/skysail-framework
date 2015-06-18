package io.skysail.server.restlet.sourceconverter;

import io.skysail.api.responses.FormResponse;
import io.skysail.server.model.ResourceModel;
import io.skysail.server.restlet.resources.SkysailServerResource;

import java.text.DateFormat;
import java.util.*;

import org.restlet.representation.Variant;

public class EntitySourceHtmlConverter extends SourceHtmlConverter {

    public EntitySourceHtmlConverter(Object source, Variant target) {
        super(source, target);
    }

    @SuppressWarnings("unchecked")
    public Map<String, Object> convert(ResourceModel<SkysailServerResource<?>, ?> resourceModel) {
        mapper.setDateFormat(DateFormat.getDateInstance(DateFormat.LONG, determineLocale(resourceModel.getResource())));
        if (source instanceof FormResponse) {
            return mapper.convertValue(((FormResponse<?>)source).getEntity(), Map.class);
        }
        throw new IllegalStateException();
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
