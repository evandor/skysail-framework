package de.twenty11.skysail.api.hooks;

import org.restlet.Request;

import de.twenty11.skysail.api.domain.Identifiable;

public interface EntityChangedHookService {

    void pushEntityWasAdded(Request request, Identifiable entity, String principal);

    void pushEntityWasChanged(Request request, Identifiable entity, String principal);

    void pushEntityWasDeleted(Request request, String principal);

}
