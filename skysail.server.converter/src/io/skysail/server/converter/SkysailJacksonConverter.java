package io.skysail.server.converter;

import java.util.*;
import java.util.stream.Collectors;

import org.osgi.service.component.annotations.Component;
import org.restlet.data.MediaType;
import org.restlet.ext.jackson.*;
import org.restlet.representation.*;
import org.restlet.resource.Resource;

import com.fasterxml.jackson.databind.ObjectWriter;
import com.fasterxml.jackson.dataformat.csv.*;

import io.skysail.api.responses.SkysailResponse;
import io.skysail.api.text.Translation;
import io.skysail.server.model.ResourceModel;
import io.skysail.server.restlet.resources.SkysailServerResource;
import io.skysail.server.services.OsgiConverterHelper;
import io.skysail.server.utils.HeadersUtils;

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
                    return "\"" + f.getId() + "\": \"" + messages.get(f.getNameKey()) + "\"";
                }).collect(Collectors.joining(","));
                HeadersUtils.getHeaders(resource.getResponse()).add("X-Resource-ColumnNames", "{" + columnNames + ",\"_links\": \"Actions\"}");


                String columns = resourceModel.getFormfields().stream().map(f -> {
                    return "\"" + f.getId() + "\"";
                }).collect(Collectors.joining(","));
                HeadersUtils.getHeaders(resource.getResponse()).add("X-Resource-Columns", "[" + columns + ",\"_links\"]");

                 return super.toRepresentation(resourceModel.getRawData(), target, resource);
            }
            if (target.getMediaType().equals(MediaType.TEXT_CSV)) {
                JacksonRepresentation<List<?>> jacksonRepresentation = new JacksonRepresentation<List<?>>(MediaType.TEXT_CSV, (List<?>)entity) {
                    @Override
                    protected ObjectWriter createObjectWriter() {
                        CsvMapper csvMapper = (CsvMapper) getObjectMapper();
                        Class<?> cls = ((List<?>)entity).get(0).getClass();
                        CsvSchema csvSchema = csvMapper.schemaFor(cls);
                        ObjectWriter result = csvMapper.writer(csvSchema);
                        return result;
                    }
                };
                return jacksonRepresentation;
            }
            return super.toRepresentation(entity, target, resource);
        }
        return super.toRepresentation(source, target, resource);
    }

}
