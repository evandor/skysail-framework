package io.skysail.server.restlet.sourceconverter;

import io.skysail.server.model.ResourceModel;
import io.skysail.server.restlet.resources.SkysailServerResource;

import org.restlet.representation.Variant;

/**
 * 
 *
 */
public interface SourceConverter {

    void init(Object source, Variant target);

    boolean isCompatible();

    Object convert(SkysailServerResource<?> resource, ResourceModel<SkysailServerResource<?>,?> resourceModel);

}
