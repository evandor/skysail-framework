package de.twenty11.skysail.server.app.sourceconverter;

import java.lang.reflect.Field;
import java.util.List;

import org.restlet.representation.Variant;

import de.twenty11.skysail.server.core.restlet.SkysailServerResource;

public interface SourceConverter {

    void init(Object source, Variant target);

    boolean isCompatible();

    Object convert(SkysailServerResource<?> resource, List<Field> fields);

}
