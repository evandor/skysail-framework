package io.skysail.server.restlet.sourceconverter;

import io.skysail.api.responses.FormResponse;
import io.skysail.server.model.ResourceModel;
import io.skysail.server.restlet.resources.SkysailServerResource;

import java.text.DateFormat;
import java.util.Map;

import org.restlet.representation.Variant;

public class EntitySourceHtmlConverter extends SourceHtmlConverter {

    public EntitySourceHtmlConverter(Object source, Variant target) {
        super(source, target);
    }

    @SuppressWarnings("unchecked")
    public Map<String, Object> convert(ResourceModel<SkysailServerResource<?>, ?> resourceModel) {
        mapper.setDateFormat(DateFormat.getDateInstance(DateFormat.LONG, determineLocale(resourceModel.getResource())));
        if (source instanceof FormResponse) {
            return mapper.convertValue(((FormResponse<?>) source).getEntity(), Map.class);
        }
        return mapper.convertValue(source, Map.class);
    }

}
