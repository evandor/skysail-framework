package de.twenty11.skysail.server.core.restlet.filter;

import java.util.Arrays;

import org.restlet.Context;
import org.restlet.Request;
import org.restlet.Response;
import org.restlet.data.MediaType;
import org.restlet.representation.Representation;
import org.restlet.representation.StringRepresentation;
import org.restlet.routing.Filter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * A Filter responsible for adding the provided LinkHeaders to the response
 *
 */
public class Linker extends Filter {

    private static final Logger logger = LoggerFactory.getLogger(Linker.class);

    public Linker(Context context) {
        super(context);
    }
    
    @Override
    protected void afterHandle(Request request, Response response) {
        StringBuilder link = new StringBuilder("<http://example.com/TheBook/chapter2>; rel=\"previous\"; title=\"previous chapter\"");
        //if (response instanceof Skysail)
        
        addLinkheaderToResponseEntity(response, link);
    }

    private void addLinkheaderToResponseEntity(Response response, StringBuilder link) {
        Representation entity = response.getEntity();
        if (entity instanceof StringRepresentation && MediaType.TEXT_HTML.equals(entity.getMediaType())) {
            ((StringRepresentation)entity).setText(response.getEntityAsText().replace("${linkheader}", linkheader(link.toString())));
        }
    }

   

    private String linkheader(String linkheader) {
        StringBuilder sb = new StringBuilder();
        if (linkheader == null || linkheader.trim().length() == 0) {
            return "";
        }
        Arrays.asList(linkheader.split(",")).forEach(link -> handleSingleLink(link.trim(), sb));
        return sb.toString();
    }

    private void handleSingleLink(String link, StringBuilder sb) {
        String[] parts = link.split(";");
        String href = parts[0].replace("<", "").replace(">","").trim();
        String title = "Title";
        String rel = "Rel";
        sb.append("<a href='").append(href).append("' rel='").append(rel).append("' title=''>").append(title).append("</a>");
    }
}
