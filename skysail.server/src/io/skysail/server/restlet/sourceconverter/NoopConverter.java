package io.skysail.server.restlet.sourceconverter;

import io.skysail.server.model.ResourceModel;
import io.skysail.server.restlet.resources.SkysailServerResource;
import de.twenty11.skysail.server.app.AbstractSourceConverter;

public class NoopConverter extends AbstractSourceConverter implements SourceConverter {

    @Override
    public boolean isCompatible() {
        return true;
    }

    @Override
    public Object convert(SkysailServerResource<?> resource, ResourceModel<SkysailServerResource<?>,?> resourceModel,  String indexPageName) {
        return getSource();
    }

}
