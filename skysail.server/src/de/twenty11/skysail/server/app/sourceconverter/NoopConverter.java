package de.twenty11.skysail.server.app.sourceconverter;

import io.skysail.server.restlet.resources.SkysailServerResource;

import java.lang.reflect.Field;
import java.util.List;

import de.twenty11.skysail.server.app.AbstractSourceConverter;

public class NoopConverter extends AbstractSourceConverter implements SourceConverter {

    @Override
    public boolean isCompatible() {
        return true;
    }

    @Override
    public Object convert(SkysailServerResource<?> resource, List<Field> fields) {
        return getSource();
    }

}
