package io.skysail.server.ext.sse.resources;

import java.io.PrintWriter;

public class LongRunningBeast implements Runnable {

    private PrintWriter out;

    public LongRunningBeast(PrintWriter out) {
        this.out = out;
    }

    public void run() {
        while (true) {
            try {
                Thread.sleep(10000);
//                StringRepresentation representation = new StringRepresentation(sb.toString());
//                representation.setMediaType(SkysailApplication.SKYSAIL_SERVER_SENT_EVENTS);
//                representation.setCharacterSet(CharacterSet.UTF_8);
               // getResponse().setEntity(representation);
               // getResponse().flushBuffers();
                out.write("data: xxx\n\n");
            } catch (InterruptedException  e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
        }
    }
}
