package de.twenty11.skysail.server.app.sourceconverter;

import de.twenty11.skysail.server.app.AbstractSourceConverter;
import de.twenty11.skysail.server.core.restlet.SkysailServerResource;


public class NoopConverter extends AbstractSourceConverter implements SourceConverter {

    @Override
    public boolean isCompatible() {
        return true;
    }

    @Override
    public Object convert(SkysailServerResource<?> resource) {
        return getSource();
    }


}
