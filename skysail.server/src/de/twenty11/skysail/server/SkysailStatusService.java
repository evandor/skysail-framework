package de.twenty11.skysail.server;

import org.restlet.Request;
import org.restlet.Response;
import org.restlet.data.Status;
import org.restlet.representation.Representation;
import org.restlet.representation.StringRepresentation;
import org.restlet.service.StatusService;

public class SkysailStatusService extends StatusService {

    @Override
    public Representation getRepresentation(Status status, Request request, Response response) {
        System.out.println(status);
        return new StringRepresentation("ooopppsss...");
    }

}
