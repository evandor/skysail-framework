package de.twenty11.skysail.server.app.sourceconverter;

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import org.restlet.data.MediaType;

import aQute.bnd.annotation.component.Component;
import de.twenty11.skysail.server.app.AbstractSourceConverter;
import de.twenty11.skysail.server.core.restlet.SkysailServerResource;

@Component
public class ListSourceHtmlConverter extends AbstractSourceConverter implements SourceConverter {

    @Override
    public boolean isCompatible() {
        return getSource() instanceof List && getTarget().getMediaType().equals(MediaType.TEXT_HTML);
    }

    @Override
    public Object convert(SkysailServerResource<?> resource) {
        List<Object> result = new ArrayList<Object>();
        for (Object object : (List<?>) getSource()) {
            if (object instanceof String) {
                result.add(object);
                continue;
            }
            if (!object.getClass().getName().contains("$$")) {
                Map<String, Object> props = mapper.convertValue(object, Map.class);
                result.add(props);
                continue;
            }
            Method[] methods = object.getClass().getMethods();
            Optional<Method> getHandlerMethod = findHandlerMethod(methods);
            if (getHandlerMethod.isPresent()) {
                result.add(mapSource(object, getHandlerMethod, resource));
            }
        }
        return result;
    }

    @Override
    protected Comparator<String> getComparator(SkysailServerResource<?> resource) {
        return new Comparator<String>() {

            @Override
            public int compare(String o1, String o2) {
                List<String> fieldNames = resource.getFields();
                if (!fieldNames.contains(o1)) {
                    fieldNames.add(o1);
                }
                if (!fieldNames.contains(o2)) {
                    fieldNames.add(o2);
                }
                return fieldNames.indexOf(o1) - fieldNames.indexOf(o2);
            }

        };
    }

}
