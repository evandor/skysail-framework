package de.twenty11.skysail.server.mgt.jmx;

import java.util.List;

import de.twenty11.skysail.api.responses.Linkheader;
import de.twenty11.skysail.server.core.restlet.ListServerResource;
import de.twenty11.skysail.server.core.restlet.ResourceContextId;
import de.twenty11.skysail.server.mgt.ManagementApplication;
import de.twenty11.skysail.server.mgt.apps.ApplicationsResource;
import de.twenty11.skysail.server.mgt.events.EventsResource;
import de.twenty11.skysail.server.mgt.load.ServerLoadResource;
import de.twenty11.skysail.server.mgt.log.LogResource;
import de.twenty11.skysail.server.mgt.peers.PeersResource;
import de.twenty11.skysail.server.mgt.time.ServerTimeResource;

public class HeapStatsResource extends ListServerResource<MemorySnapshot> {

    private ManagementApplication app;

    public HeapStatsResource() {
        super(null);
        app = (ManagementApplication) getApplication();
        addToContext(ResourceContextId.LINK_TITLE, "Heap Stats");
    }

    @Override
    public List<MemorySnapshot> getData() {
        return app.getJmxMonitor().getHeapStats();
    }

    @Override
    public List<Linkheader> getLinkheader() {
        return super.getLinkheader(PeersResource.class, LogResource.class, EventsResource.class, HeapStatsResource.class,
                ServerLoadResource.class, ServerTimeResource.class, ApplicationsResource.class);
    }

}
