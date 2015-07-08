package io.skysail.server.converter;

import io.skysail.api.responses.SkysailResponse;
import io.skysail.server.model.ResourceModel;
import io.skysail.server.restlet.resources.SkysailServerResource;

import org.restlet.ext.jackson.JacksonConverter;
import org.restlet.representation.*;
import org.restlet.resource.Resource;

import aQute.bnd.annotation.component.Component;
import de.twenty11.skysail.server.services.OsgiConverterHelper;

@Component(immediate = true)
public class SkysailJacksonConverter extends JacksonConverter implements OsgiConverterHelper {

    @Override
    public Representation toRepresentation(Object source, Variant target, Resource resource) {
        if (source instanceof SkysailResponse) {
            Object entity = ((SkysailResponse<?>)source).getEntity();
            if (resource.getQuery().getNames().contains("_rendered")) {
                ResourceModel<SkysailServerResource<?>,?> resourceModel = new ResourceModel((SkysailServerResource<?>) resource, (SkysailResponse<?>)source, target);
                System.out.println(resourceModel.toString());
            }
            return super.toRepresentation(entity, target, resource);
        }
        return super.toRepresentation(source, target, resource);
    }

}
