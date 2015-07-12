package io.skysail.server.converter;

import io.skysail.api.responses.SkysailResponse;
import io.skysail.server.model.ResourceModel;
import io.skysail.server.restlet.resources.SkysailServerResource;
import io.skysail.server.utils.HeadersUtils;

import java.util.Map;

import org.restlet.data.Header;
import org.restlet.ext.jackson.JacksonConverter;
import org.restlet.representation.*;
import org.restlet.resource.Resource;
import org.restlet.util.Series;

import aQute.bnd.annotation.component.Component;
import de.twenty11.skysail.server.services.OsgiConverterHelper;

@Component(immediate = true)
public class SkysailJacksonConverter extends JacksonConverter implements OsgiConverterHelper {

    @Override
    public Representation toRepresentation(Object source, Variant target, Resource resource) {
        if (source instanceof SkysailResponse) {
            Object entity = ((SkysailResponse<?>)source).getEntity();
            if (resource.getQuery().getNames().contains("_rendered")) {
                SkysailServerResource<?> skysailServerResource = (SkysailServerResource<?>) resource;
                ResourceModel<SkysailServerResource<?>,?> resourceModel = new ResourceModel(skysailServerResource, (SkysailResponse<?>)source, target);
                
                //String key = resource.getClass().getName() + ".message";
                //String translated = ((SkysailApplication) application).translate(key, key, this, true);
                Map<String, String> messages = skysailServerResource.getMessages(resourceModel.getFields());
                String descrition = messages.get("content.header");
                Series<Header> headers = HeadersUtils.getHeaders(resource.getResponse());
                HeadersUtils.getHeaders(resource.getResponse()).add("X-Resource-Description", descrition);
                
                return super.toRepresentation(resourceModel.getData(), target, resource);
            }
            return super.toRepresentation(entity, target, resource);
        }
        return super.toRepresentation(source, target, resource);
    }

}
