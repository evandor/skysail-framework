package io.skysail.server.converter;

import io.skysail.api.responses.EntityServerResponse;

import org.restlet.ext.jackson.JacksonConverter;
import org.restlet.representation.*;
import org.restlet.resource.Resource;

import aQute.bnd.annotation.component.Component;
import de.twenty11.skysail.server.services.OsgiConverterHelper;

@Component(immediate = true)
public class SkysailJacksonConverter extends JacksonConverter implements OsgiConverterHelper {

    @Override
    public Representation toRepresentation(Object source, Variant target, Resource resource) {
        if (source instanceof EntityServerResponse) {
            return super.toRepresentation(((EntityServerResponse<?>)source).getEntity(), target, resource);
        }
        return super.toRepresentation(source, target, resource);
    }

}
