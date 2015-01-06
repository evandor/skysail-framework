package de.twenty11.skysail.server.app.sourceconverter;

import java.lang.reflect.Method;
import java.util.Optional;

import org.restlet.data.MediaType;

import aQute.bnd.annotation.component.Component;
import de.twenty11.skysail.api.responses.SkysailResponse;
import de.twenty11.skysail.server.app.AbstractSourceConverter;
import de.twenty11.skysail.server.core.restlet.SkysailServerResource;

@Component
public class SkysailResponseJsonConverter extends AbstractSourceConverter implements SourceConverter {

    @Override
    public boolean isCompatible() {
        return getSource() instanceof SkysailResponse && getTarget().getMediaType().equals(MediaType.APPLICATION_JSON);
    }

    @Override
    public Object convert(SkysailServerResource<?> resource) {
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
