package de.twenty11.skysail.server.mgt.log;

import org.osgi.service.log.LogReaderService;

import aQute.bnd.annotation.component.Component;
import aQute.bnd.annotation.component.Reference;

@Component
public class LogServiceProvider {

    private static volatile LogReaderService service;

    @Reference(dynamic = true, optional = true)
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
