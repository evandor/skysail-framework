package io.skysail.server.restlet.sourceconverter;

import io.skysail.api.responses.SkysailResponse;
import io.skysail.server.restlet.resources.SkysailServerResource;
import io.skysail.server.utils.OrientDbUtils;

import java.lang.reflect.Field;
import java.util.*;

import org.restlet.data.MediaType;

import aQute.bnd.annotation.component.Component;
import de.twenty11.skysail.server.app.AbstractSourceConverter;

@Component
public class SkysailResponseJsonConverter extends AbstractSourceConverter implements SourceConverter {

    @Override
    public boolean isCompatible() {
        return getSource() instanceof SkysailResponse && getTarget().getMediaType().equals(MediaType.APPLICATION_JSON);
    }

    @Override
    public Object convert(SkysailServerResource<?> resource, List<Field> fields, String indexPageName) {
        Object object = ((SkysailResponse<?>) getSource()).getEntity();
        if (object == null || !object.getClass().getName().contains("$$")) {
            return object;
        }
        Map<String, Object> map = OrientDbUtils.toMap(object);
        if (map != null) {
            return mapSource2(object, map, resource);
        }
        return getSource();
    }

}
