package io.skysail.server.ext.sse.resources;

import io.skysail.server.app.SkysailApplication;
import io.skysail.server.ext.sse.*;

import java.io.*;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.Collectors;

import org.apache.shiro.SecurityUtils;
import org.restlet.representation.*;
import org.restlet.resource.*;

public class GuiMessageResource extends ServerResource {

    private SseApplication app;

    @Override
    protected void doInit() {
        app = (SseApplication) getApplication();
    }

    @Override
    protected Representation get() throws ResourceException {

        String username = SecurityUtils.getSubject().getPrincipal().toString();
        List<Message> messages = app.getEvents(username);

        int retryInMillis = 20000 / (1 + messages.size());

        StringBuilder sb = new StringBuilder("retry: ").append(retryInMillis).append("\n");
        sb.append(messages.stream().map(m -> {
            return "id: ".concat(m.getId().toString()).concat("\n")
            // .concat("event: ").concat(m.getType()).concat("\n")
                    .concat("data: ").concat(m.getMessage());
        }).collect(Collectors.joining("\n")));
        sb.append("\n\n");

        System.out.println(sb.toString());


        Representation representation = new WriterRepresentation(SkysailApplication.SKYSAIL_SERVER_SENT_EVENTS) {

            AtomicInteger i = new AtomicInteger();

            @Override
            public void write(Writer writer) throws IOException {
                String json = "{\"foo\" : \"bar\"}";
                while (true) {
                    try {
                        writer.write("data: hier (" + i.incrementAndGet() + ")\n\n");
                        writer.flush();
                        Thread.sleep(5000);
                        
                    } catch (InterruptedException e) {
                        // TODO Auto-generated catch block
                        e.printStackTrace();
                    }
                    // if (i.incrementAndGet() % 1000 == 1) {
                    //writer.write("data: hier" + i.incrementAndGet() + "\n\n");
                    // }
                }
            }

        };

        return representation;
    }

}
