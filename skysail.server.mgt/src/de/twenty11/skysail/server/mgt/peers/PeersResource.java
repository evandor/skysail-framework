//package de.twenty11.skysail.server.mgt.peers;
//
//import java.util.ArrayList;
//import java.util.List;
//
//import de.twenty11.skysail.api.responses.Linkheader;
//import de.twenty11.skysail.server.core.restlet.ListServerResource;
//import de.twenty11.skysail.server.mgt.ManagementApplication;
//import de.twenty11.skysail.server.mgt.apps.ApplicationsResource;
//import de.twenty11.skysail.server.mgt.events.EventsResource;
//import de.twenty11.skysail.server.mgt.jmx.HeapStatsResource;
//import de.twenty11.skysail.server.mgt.load.ServerLoadResource;
//import de.twenty11.skysail.server.mgt.log.LogResource;
//import de.twenty11.skysail.server.mgt.performance.PerformanceResource;
//import de.twenty11.skysail.server.mgt.time.ServerTimeResource;
//
//public class PeersResource extends ListServerResource<Peer> {
//
//    private volatile ManagementApplication app;
//
//    public PeersResource() {
//        super(null);
//        app = (ManagementApplication) getApplication();
//    }
//
//    @Override
//    public List<Peer> getData() {
//        List<Peer> result = new ArrayList<Peer>();
//        return result;
//    }
//
//    @Override
//    public List<Linkheader> getLinkheader() {
//        return super.getLinkheader(LogResource.class, EventsResource.class, HeapStatsResource.class,
//                ServerLoadResource.class, ServerTimeResource.class, ApplicationsResource.class, PerformanceResource.class);
//    }
//
//}
