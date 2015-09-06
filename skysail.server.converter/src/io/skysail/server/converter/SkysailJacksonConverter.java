package io.skysail.server.converter;

import io.skysail.api.responses.SkysailResponse;
import io.skysail.api.text.Translation;
import io.skysail.server.model.ResourceModel;
import io.skysail.server.restlet.resources.SkysailServerResource;
import io.skysail.server.utils.HeadersUtils;

import java.util.Map;
import java.util.stream.Collectors;

import org.restlet.ext.jackson.JacksonConverter;
import org.restlet.representation.*;
import org.restlet.resource.Resource;

import aQute.bnd.annotation.component.Component;
import de.twenty11.skysail.server.services.OsgiConverterHelper;

@Component(immediate = true)
public class SkysailJacksonConverter extends JacksonConverter implements OsgiConverterHelper { // NO_UCD

    @Override
    public Representation toRepresentation(Object source, Variant target, Resource resource) {
        if (source instanceof SkysailResponse) {
            Object entity = ((SkysailResponse<?>) source).getEntity();
            if (resource.getQuery().getNames().contains("_rendered")) {
                SkysailServerResource<?> skysailServerResource = (SkysailServerResource<?>) resource;


                @SuppressWarnings({ "rawtypes", "unchecked" })
                ResourceModel<SkysailServerResource<?>, ?> resourceModel = new ResourceModel(skysailServerResource, (SkysailResponse<?>) source, target);

                Map<String, Translation> messages = skysailServerResource.getMessages(resourceModel.getFields());
                Translation descrition = messages.get("content.header");
                HeadersUtils.getHeaders(resource.getResponse()).add("X-Resource-Description", descrition.getTranslated());

                String columnNames = resourceModel.getFormfields().stream().map(f -> {
                    return "\"" + f.getName() + "\": \"" + messages.get(f.getNameKey()) + "\"";
                }).collect(Collectors.joining(","));
                HeadersUtils.getHeaders(resource.getResponse()).add("X-Resource-ColumnNames", "{" + columnNames + ",\"_links\": \"Actions\"}");


                String columns = resourceModel.getFormfields().stream().map(f -> {
                    return "\"" + f.getName() + "\"";
                }).collect(Collectors.joining(","));
                HeadersUtils.getHeaders(resource.getResponse()).add("X-Resource-Columns", "[" + columns + ",\"_links\"]");

                return super.toRepresentation(resourceModel.getRawData(), target, resource);
            }
            return super.toRepresentation(entity, target, resource);
        }
        return super.toRepresentation(source, target, resource);
    }

}
