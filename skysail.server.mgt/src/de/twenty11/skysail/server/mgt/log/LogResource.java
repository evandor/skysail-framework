package de.twenty11.skysail.server.mgt.log;

import io.skysail.server.restlet.resources.ListServerResource;

import java.util.*;

import org.osgi.service.log.LogReaderService;
import org.restlet.resource.ResourceException;

import de.twenty11.skysail.server.core.restlet.ResourceContextId;

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
    public List<LogEntry> getEntity() {
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

//    @Override
//    public List<Linkheader> getLinkheader() {
//        return super.getLinkheader(PeersResource.class, EventsResource.class, HeapStatsResource.class,
//                ServerLoadResource.class, ServerTimeResource.class, ApplicationsResource.class);
//    }


}
