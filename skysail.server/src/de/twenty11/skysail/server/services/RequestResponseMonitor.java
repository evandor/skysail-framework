package de.twenty11.skysail.server.services;

import org.restlet.Request;
import org.restlet.Response;

import aQute.bnd.annotation.ProviderType;
import de.twenty11.skysail.server.core.restlet.request.RequestDescriptor;
import de.twenty11.skysail.server.core.restlet.response.ResponseDescriptor;

@ProviderType
public interface RequestResponseMonitor {

    long monitor(Request request);

    void monitor(Request request, Response response);

    RequestDescriptor getRequest(long id);

    ResponseDescriptor getResponse(long id);
}
