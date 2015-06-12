package io.skysail.server.ext.sse.resources;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.restlet.ext.servlet.ServerServlet;
import org.restlet.ext.servlet.ServletAdapter;

public class SseServlet2 extends ServerServlet {
    
    public SseServlet2() {
        System.out.println("hier");
    }
    
    private ServletAdapter adapter;
   // private final Restlet restlet;

//    public SseServlet2(Restlet restlet) {
//        this.restlet = restlet;
//    }
//
//    @Override
//    public void init() throws ServletException {
//        super.init();
//        this.adapter = new ServletAdapter(getServletContext());
//        this.restlet.setContext(this.adapter.getContext());
//        this.adapter.setNext(this.restlet);
//    }

    
    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        super.doGet(req, resp);
    }
    @Override
    public void service(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        this.adapter.service(req, resp);
    }

//    private SseApplication app;
//    
//    private static String dummy = "";
//
//    @Override
//    protected void doInit() {
//        app = (SseApplication) getApplication();
//    }
//
//    @Override
//    protected Representation get() throws ResourceException {
//
//        String username = SecurityUtils.getSubject().getPrincipal().toString();
//        List<Message> messages = app.getEvents(username);
//        
//
//        int retryInMillis = 20000 / (1 + messages.size());    
//        
//        if (messages.size() > 0) {
//            dummy = "";
//        }
//        
//        StringBuilder sb = new StringBuilder("retry: ").append(retryInMillis).append("\n");
//        dummy += ".";
//        //sb.append("data: ").append(dummy).append("\n");
//        sb.append(messages.stream().map(m -> {
//            return "id: ".concat(m.getId().toString()).concat("\n")
//                    //.concat("event: ").concat(m.getType()).concat("\n")
//                    .concat("data: ").concat(m.getMessage());
//        }).collect(Collectors.joining("\n")));
//        sb.append("\n\n");
//        
//        System.out.println(sb.toString());
//        
//       
//        
//        Thread myThread = new Thread() {
//            @Override
//            public void run() {
//                while (true) {
//                    try {
//                        sleep(10000);
//                        StringRepresentation representation = new StringRepresentation(sb.toString());
//                        representation.setMediaType(SkysailApplication.SKYSAIL_SERVER_SENT_EVENTS);
//                        representation.setCharacterSet(CharacterSet.UTF_8);
//                        getResponse().setEntity(representation);
//                        getResponse().flushBuffers();
//                    } catch (InterruptedException | IOException e) {
//                        // TODO Auto-generated catch block
//                        e.printStackTrace();
//                    }
//                }
//            }
//        };
//        myThread.start();
//        
//        StringRepresentation representation = new StringRepresentation("data: done...\n\n");
//        representation.setMediaType(SkysailApplication.SKYSAIL_SERVER_SENT_EVENTS);
//        representation.setCharacterSet(CharacterSet.UTF_8);
//        return representation;
//    }

}
