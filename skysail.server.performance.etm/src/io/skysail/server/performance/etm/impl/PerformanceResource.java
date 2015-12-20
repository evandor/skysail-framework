package io.skysail.server.performance.etm.impl;

import io.skysail.api.responses.SkysailResponse;
import io.skysail.domain.Identifiable;
import io.skysail.server.restlet.resources.EntityServerResource;

import java.io.StringWriter;

import de.twenty11.skysail.server.core.restlet.ResourceContextId;
import etm.contrib.renderer.SimpleHtmlRenderer;
import etm.core.monitor.EtmMonitor;

public class PerformanceResource extends EntityServerResource<Identifiable> {

    private PerformanceApplication app;

    public PerformanceResource() {
        addToContext(ResourceContextId.LINK_TITLE, "Performance");
        app = (PerformanceApplication) getApplication();
    }

    @Override
    public String getId() {
        return null;
    }

    @Override
    public Identifiable getEntity() {
        EtmMonitor monitor = app.getMonitor();
        StringWriter writer = new StringWriter();
        monitor.render(new SimpleHtmlRenderer(writer));
        return new Identifiable() {

            @Override
            public void setId(String id) {
            }

            @Override
            public String getId() {
                return null;
            }
        };//writer.toString();
    }

    @Override
    public SkysailResponse<?> eraseEntity() {
        return null;
    }

}
