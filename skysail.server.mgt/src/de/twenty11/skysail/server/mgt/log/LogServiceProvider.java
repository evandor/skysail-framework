package de.twenty11.skysail.server.mgt.log;

import org.osgi.service.component.annotations.*;
import org.osgi.service.log.LogReaderService;

@Component
public class LogServiceProvider {

    private static volatile LogReaderService service;

    @Reference(policy = ReferencePolicy.DYNAMIC, cardinality = ReferenceCardinality.OPTIONAL)
    public void setLogReaderService(LogReaderService service) {
        LogServiceProvider.service = service;
    }

    public void unsetLogReaderService(LogReaderService service) {
        LogServiceProvider.service = null;
    }

    public static LogReaderService getLogReaderService() {
        return service;
    }

}
