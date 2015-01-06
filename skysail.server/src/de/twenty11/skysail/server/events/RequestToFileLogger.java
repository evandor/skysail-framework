package de.twenty11.skysail.server.events;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.text.SimpleDateFormat;
import java.util.Date;

import org.apache.shiro.SecurityUtils;
import org.apache.shiro.subject.Subject;
import org.osgi.service.event.Event;
import org.osgi.service.event.EventHandler;
import org.slf4j.Logger;

import de.twenty11.skysail.server.core.osgi.internal.EventHelper;

//@Component(immediate = true, properties = { "event.topics=request/*" })
public class RequestToFileLogger implements EventHandler {

    private static final Logger log = org.slf4j.LoggerFactory.getLogger(RequestToFileLogger.class);

    private static final String logFilePath = "requests.txt";

    private static PrintWriter out;

    static {
        try {
            // TODO close out (where?)
            out = new PrintWriter(new BufferedWriter(new FileWriter(logFilePath, true)));
        } catch (IOException e) {
            log.error(e.getMessage());
        }

    }

    public RequestToFileLogger() {
        System.out.println("tst");
    }

    @Override
    public void handleEvent(Event event) {
        String path = (String) event.getProperty(EventHelper.EVENT_PROPERTY_PATH);
        String method = (String) event.getProperty(EventHelper.EVENT_PROPERTY_METHOD);
        logToFile(path, method);
    }

    private void logToFile(String path, String method) {
        StringBuilder sb = new StringBuilder(new SimpleDateFormat("dd.MM.yyyy HH:mm:ss").format(new Date()));
        Subject subject = SecurityUtils.getSubject();

        String username = (String) subject.getPrincipal();
        sb.append(" ").append(username).append(" - ").append(method).append(" - ").append(path);
        if (out == null) {
            return;
        }
        out.println(sb.toString());
        out.flush();// NOT PERFORMANT FOR SURE
    }

}
