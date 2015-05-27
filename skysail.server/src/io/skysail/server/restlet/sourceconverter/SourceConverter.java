package io.skysail.server.restlet.sourceconverter;

import io.skysail.server.restlet.resources.SkysailServerResource;

import java.lang.reflect.Field;
import java.util.List;

import org.restlet.representation.Variant;

/**
 * 
 *
 */
public interface SourceConverter {

    void init(Object source, Variant target);

    boolean isCompatible();

    Object convert(SkysailServerResource<?> resource, List<Field> fields, String indexPageName);

}
