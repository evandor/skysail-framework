package de.twenty11.skysail.api.services;

import java.util.List;

import aQute.bnd.annotation.ProviderType;
import de.twenty11.skysail.api.services.logentry.FrameworkEventLogEntry;

@ProviderType
public interface FrameworkEventsProvider {

	  List<FrameworkEventLogEntry> getNewestEvents();

	    void clear();
}
