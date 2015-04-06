package de.twenty11.skysail.server.app.sourceconverter;

import io.skysail.api.responses.SkysailResponse;
import io.skysail.server.restlet.resources.SkysailServerResource;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.List;
import java.util.Optional;

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
    public Object convert(SkysailServerResource<?> resource, List<Field> fields) {
        Object object = ((SkysailResponse<?>) getSource()).getEntity();
        if (object == null || !object.getClass().getName().contains("$$")) {
            return object;
        }
        Method[] methods = object.getClass().getMethods();
        Optional<Method> getHandlerMethod = findHandlerMethod(methods);
        if (getHandlerMethod.isPresent()) {
            return mapSource(object, getHandlerMethod, resource);
        }
        return getSource();
    }

}
