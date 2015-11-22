package de.twenty11.skysail.server.mgt.jmx;

import io.skysail.server.restlet.resources.ListServerResource;

import java.util.List;

import de.twenty11.skysail.server.core.restlet.ResourceContextId;
import de.twenty11.skysail.server.mgt.ManagementApplication;

public class HeapStatsResource extends ListServerResource<MemorySnapshot> {

    private ManagementApplication app;

    public HeapStatsResource() {
        super(null);
        app = (ManagementApplication) getApplication();
        addToContext(ResourceContextId.LINK_TITLE, "Heap Stats");
    }

    @Override
    public List<MemorySnapshot> getEntity() {
        return app.getJmxMonitor().getHeapStats();
    }

//    @Override
//    public List<Linkheader> getLinkheader() {
//        return super.getLinkheader(PeersResource.class, LogResource.class, EventsResource.class, HeapStatsResource.class,
//                ServerLoadResource.class, ServerTimeResource.class, ApplicationsResource.class);
//    }

}
