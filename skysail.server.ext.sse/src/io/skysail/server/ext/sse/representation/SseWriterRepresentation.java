package io.skysail.server.ext.sse.representation;

import io.skysail.server.app.SkysailApplication;
import io.skysail.server.ext.sse.*;

import java.io.*;
import java.util.*;
import java.util.stream.Collectors;

import lombok.extern.slf4j.Slf4j;

import org.restlet.representation.WriterRepresentation;

@Slf4j
public class SseWriterRepresentation extends WriterRepresentation {

    private String username;
    private SseApplication app;
    private boolean initiated = true;

    public SseWriterRepresentation(String username, SseApplication app) {
        super(SkysailApplication.SKYSAIL_SERVER_SENT_EVENTS);
        this.username = username;
        this.app = app;
    }

    @Override
    public void write(Writer writer) throws IOException {
        while (true) {
            try {
                List<Message> messages;
                if (!initiated) {
                    messages = new ArrayList<>();
                    messages.add(new Message("connected...", "success"));
                    initiated = true;
                } else {
                    messages = app.getEvents(username);
                }
                if (messages.size() > 0) {
                    StringBuilder sb = new StringBuilder("retry: 10000").append("\n");
                    sb.append(messages.stream().map(m -> {
                        StringBuilder b = new StringBuilder();
                        if (m.getId() != null) {
                            b.append("id: ").append(m.getId().toString()).append("\n");
                        }
                        b.append("data: ").append(m.getMessage());
                        // .concat("event: ").concat(m.getType()).concat("\n")
                            return b.toString();
                        }).collect(Collectors.joining("\n")));
                    sb.append("\n\n");

                    writer.write(sb.toString());
                    writer.flush();
                }
                Thread.sleep(5000);
            } catch (InterruptedException e) {
                log.warn(e.getMessage());
            }
        }
    }

}
