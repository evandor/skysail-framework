package de.twenty11.skysail.api.services;

import java.util.List;

import aQute.bnd.annotation.ProviderType;
import de.twenty11.skysail.api.services.logentry.ServiceEventLogEntry;

@ProviderType
public interface ServiceEventsProvider {

    List<ServiceEventLogEntry> getNewestEvents();

    void clear();

}
