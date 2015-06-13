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

    private static String dummy = "";

    @Override
    protected void doInit() {
        app = (SseApplication) getApplication();
    }

    @Override
    protected Representation get() throws ResourceException {

        String username = SecurityUtils.getSubject().getPrincipal().toString();
        List<Message> messages = app.getEvents(username);

        int retryInMillis = 20000 / (1 + messages.size());

        if (messages.size() > 0) {
            dummy = "";
        }

        StringBuilder sb = new StringBuilder("retry: ").append(retryInMillis).append("\n");
        dummy += ".";
        // sb.append("data: ").append(dummy).append("\n");
        sb.append(messages.stream().map(m -> {
            return "id: ".concat(m.getId().toString()).concat("\n")
            // .concat("event: ").concat(m.getType()).concat("\n")
                    .concat("data: ").concat(m.getMessage());
        }).collect(Collectors.joining("\n")));
        sb.append("\n\n");

        System.out.println(sb.toString());

        // StringRepresentation representation = new
        // StringRepresentation(sb.toString());
        // representation.setMediaType(SkysailApplication.SKYSAIL_SERVER_SENT_EVENTS);
        // representation.setCharacterSet(CharacterSet.UTF_8);

//        PipedInputStream pi = new PipedInputStream();
//        PipedOutputStream po;
//        try {
//            po = new PipedOutputStream(pi);
//            Representation ir = new OutputRepresentation(SkysailApplication.SKYSAIL_SERVER_SENT_EVENTS) {
//                @Override
//                public void write(OutputStream realOutput) throws IOException {
//                    byte[] b = new byte[8];
//                    int read;
//                    while ((read = pi.read(b)) != -1) {
//                        realOutput.write(b, 0, read);
//                        realOutput.flush();
//                    }
//                }
//            };
//           // representation.setCharacterSet(CharacterSet.UTF_8);
//            OutputStreamWriter ow = new OutputStreamWriter(po);
//            PrintWriter out = new PrintWriter(ow, true);
//            new Thread(new LongRunningBeast(out)).start();
//            return ir;
//        } catch (IOException e1) {
//            e1.printStackTrace();
//        }

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

//            @Override
//            public void write(OutputStream writer) throws IOException {
//                while (true) {
//                    try {
//                        String str = "data: hier2 (" + i.incrementAndGet() + ")\n\n";
//                        byte[] bytes = str.getBytes();
//                        writer.write(bytes);
//                        writer.flush();
//                        Thread.sleep(150);
//                        
//                    } catch (InterruptedException e) {
//                        // TODO Auto-generated catch block
//                        e.printStackTrace();
//                    }
//                    // if (i.incrementAndGet() % 1000 == 1) {
//                    //writer.write("data: hier" + i.get() + "\n\n");
//                    // }
//                }
//            }
        };

        return representation;
        // return new StringRepresentation
        // ("error creating server sent events");
    }

}
