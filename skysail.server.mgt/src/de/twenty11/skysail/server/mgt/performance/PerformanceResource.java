package de.twenty11.skysail.server.mgt.performance;

import io.skysail.api.links.Link;
import io.skysail.api.responses.SkysailResponse;
import io.skysail.server.ResourceContextId;
import io.skysail.server.restlet.resources.EntityServerResource;

import java.io.StringWriter;
import java.util.List;

import de.twenty11.skysail.server.mgt.ManagementApplication;
import de.twenty11.skysail.server.mgt.apps.ApplicationsResource;
import etm.contrib.renderer.SimpleHtmlRenderer;
import etm.core.monitor.EtmMonitor;

public class PerformanceResource extends EntityServerResource<PerformanceInfo> {

	private ManagementApplication app;

	public PerformanceResource() {
		addToContext(ResourceContextId.LINK_TITLE, "Performance");
		app = (ManagementApplication)getApplication();
    }

	@Override
	public PerformanceInfo getEntity() {
		EtmMonitor monitor = app.getMonitor();
		StringWriter writer = new StringWriter();
		monitor.render(new SimpleHtmlRenderer(writer));
		return new PerformanceInfo(writer.toString());
	}

	@Override
	public String getId() {
		return null;
	}

	@Override
	public SkysailResponse<?> eraseEntity() {
		return null;
	}

	@Override
	public List<Link> getLinks() {
	    return super.getLinks(ApplicationsResource.class);
	}

}
