package de.twenty11.skysail.server.app.sourceconverter;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;

import org.restlet.data.MediaType;

import de.twenty11.skysail.server.app.AbstractSourceConverter;
import de.twenty11.skysail.server.core.restlet.SkysailServerResource;

//@Component
public class ListSourceJsonConverter extends AbstractSourceConverter implements SourceConverter {

    @Override
    public boolean isCompatible() {
        return getSource() instanceof List && getTarget().getMediaType().equals(MediaType.APPLICATION_JSON);
    }

    @Override
    public Object convert(SkysailServerResource<?> resource, List<Field> fields) {
        List<Object> result = new ArrayList<Object>();
        // for (Object object : (List<?>) getSource()) {
        // if (!object.getClass().getName().contains("$$")) {
        // result.add(object);
        // continue;
        // }
        // Method[] methods = object.getClass().getMethods();
        // Optional<Method> getHandlerMethod = findHandlerMethod(methods);
        // if (getHandlerMethod.isPresent()) {
        // result.add(mapSource(object, getHandlerMethod));
        // }
        // }
        return result;
    }

}
