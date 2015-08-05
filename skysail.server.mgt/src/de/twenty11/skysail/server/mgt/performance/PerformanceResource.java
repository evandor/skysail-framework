package de.twenty11.skysail.server.mgt.performance;

import java.io.StringWriter;

import de.twenty11.skysail.api.responses.SkysailResponse;
import de.twenty11.skysail.server.core.restlet.EntityServerResource;
import de.twenty11.skysail.server.core.restlet.ResourceContextId;
import de.twenty11.skysail.server.mgt.ManagementApplication;
import etm.contrib.renderer.SimpleHtmlRenderer;
import etm.core.monitor.EtmMonitor;

public class PerformanceResource extends EntityServerResource<String> {

	private ManagementApplication app;

	public PerformanceResource() {
		addToContext(ResourceContextId.LINK_TITLE, "Performance");
		app = (ManagementApplication)getApplication();
    }
	
	@Override
	public String getData() {
		EtmMonitor monitor = app.getMonitor();
		StringWriter writer = new StringWriter();
		monitor.render(new SimpleHtmlRenderer(writer));
		return writer.toString();
	}

	@Override
	public String getId() {
		return null;
	}

	@Override
	public SkysailResponse<?> eraseEntity() {
		return null;
	}

}
