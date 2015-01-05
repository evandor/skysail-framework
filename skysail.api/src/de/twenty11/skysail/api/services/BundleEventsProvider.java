package de.twenty11.skysail.api.services;

import java.util.List;

import aQute.bnd.annotation.ProviderType;
import de.twenty11.skysail.api.services.logentry.BundleEventLogEntry;

@ProviderType
public interface BundleEventsProvider {

    List<BundleEventLogEntry> getNewestEvents();

    void clear();

}
