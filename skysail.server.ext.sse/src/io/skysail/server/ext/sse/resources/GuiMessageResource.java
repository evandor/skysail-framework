package io.skysail.server.ext.sse.resources;

import io.skysail.server.app.SkysailApplication;
import io.skysail.server.ext.sse.*;

import java.util.List;
import java.util.stream.Collectors;

import org.apache.shiro.SecurityUtils;
import org.restlet.data.CharacterSet;
import org.restlet.representation.*;
import org.restlet.resource.*;

public class GuiMessageResource extends ServerResource {

    private SseApplication app;
    
    private static String dummy = "";

    @Override
    protected void doInit() {
        app = (SseApplication) getApplication();
    }

    @Override
    protected Representation get() throws ResourceException {

        String username = SecurityUtils.getSubject().getPrincipal().toString();
        List<Message> messages = app.getEvents(username);

        int retryInMillis = 10000 / (1 + messages.size());    
        
        if (messages.size() > 0) {
            dummy = "";
        }
        
        StringBuilder sb = new StringBuilder("retry: ").append(retryInMillis).append("\n");
        dummy += ".";
        //sb.append("data: ").append(dummy).append("\n");
        sb.append(messages.stream().map(m -> {
            return "id: ".concat(m.getId().toString()).concat("\n")
                    //.concat("event: ").concat(m.getType()).concat("\n")
                    .concat("data: ").concat(m.getMessage());
        }).collect(Collectors.joining("\n")));
        sb.append("\n\n");
        
       // System.out.println(sb.toString());
        
        StringRepresentation representation = new StringRepresentation(sb.toString());
        representation.setMediaType(SkysailApplication.SKYSAIL_SERVER_SENT_EVENTS);
        representation.setCharacterSet(CharacterSet.UTF_8);
        return representation;
    }

}
