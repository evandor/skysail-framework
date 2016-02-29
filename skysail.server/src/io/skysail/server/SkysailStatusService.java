package io.skysail.server;

import org.restlet.Request;
import org.restlet.Response;
import org.restlet.data.MediaType;
import org.restlet.data.Status;
import org.restlet.representation.Representation;
import org.restlet.representation.StringRepresentation;
import org.restlet.service.StatusService;

public class SkysailStatusService extends StatusService {

    @Override
    public Representation toRepresentation(Status status, Request request, Response response) {
        String msg = "<u>Sorry, there was a problem processing your request:</u><br><br>";
        msg += status.toString() + "<br><br><br>";
        msg += "Proceed <a href='/' onClick='javascript:document.cookie=\"mainpage=index;path=/\";'>here</a> to get back to the application.";
        return new StringRepresentation(msg, MediaType.TEXT_HTML);
    }
}
