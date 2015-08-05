package de.twenty11.skysail.server.mgt.log;

import java.util.ArrayList;
import java.util.Enumeration;
import java.util.List;

import org.osgi.service.log.LogReaderService;
import org.restlet.resource.ResourceException;

import de.twenty11.skysail.api.responses.Linkheader;
import de.twenty11.skysail.server.core.restlet.ListServerResource;
import de.twenty11.skysail.server.core.restlet.ResourceContextId;
import de.twenty11.skysail.server.mgt.apps.ApplicationsResource;
import de.twenty11.skysail.server.mgt.events.EventsResource;
import de.twenty11.skysail.server.mgt.jmx.HeapStatsResource;
import de.twenty11.skysail.server.mgt.load.ServerLoadResource;
import de.twenty11.skysail.server.mgt.peers.PeersResource;
import de.twenty11.skysail.server.mgt.time.ServerTimeResource;

public class LogResource extends ListServerResource<LogEntry> {

    private LogReaderService logReaderService;

    public LogResource() {
        super(null);
        addToContext(ResourceContextId.LINK_TITLE, "Logs");
    }

    @Override
    protected void doInit() throws ResourceException {
        logReaderService = LogServiceProvider.getLogReaderService();
    }

    @SuppressWarnings("unchecked")
    @Override
    public List<LogEntry> getData() {
        List<LogEntry> result = new ArrayList<LogEntry>();
        if (logReaderService != null) {
            Enumeration<org.osgi.service.log.LogEntry> log = logReaderService.getLog();
            while (log.hasMoreElements()) {
                org.osgi.service.log.LogEntry nextElement = log.nextElement();
                result.add(new LogEntry(nextElement));
            }
        } else {
            result.add(new LogEntry("No LogServiceProvider available..."));
        }
        return result;
    }

    @Override
    public List<Linkheader> getLinkheader() {
        return super.getLinkheader(PeersResource.class, EventsResource.class, HeapStatsResource.class,
                ServerLoadResource.class, ServerTimeResource.class, ApplicationsResource.class);
    }


}
