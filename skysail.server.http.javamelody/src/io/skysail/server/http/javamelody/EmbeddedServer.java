package io.skysail.server.http.javamelody;

import java.util.*;

import javax.servlet.*;

import lombok.extern.slf4j.Slf4j;
import net.bull.javamelody.Parameter;

import org.eclipse.jetty.server.*;
import org.eclipse.jetty.server.handler.*;
import org.eclipse.jetty.servlet.*;

import aQute.bnd.annotation.component.*;

@Component(immediate = true, configurationPolicy = ConfigurationPolicy.require, properties = { "service.pid=javamelody" })
@Slf4j
public class EmbeddedServer {

    private Server server;

    @Activate
    public void activate(Map<String, String> config) {
        if (config == null || config.size() == 0) {
            log.warn("could not configure javamelody, configuration missing or empty");
            return;
        }

        String javamelodyPort = config.get("server-port");
        log.info("about to start javamelody on port '{}'", javamelodyPort);
        server = new Server(Integer.parseInt(javamelodyPort));
        init(config);
        start();
    }

    private void start() {
        try {
            server.start();
        } catch (Exception e) {
            log.error(e.getMessage(), e);
        }
    }

    private void init(Map<String, String> config) {

        ContextHandlerCollection contexts = new ContextHandlerCollection();
        ServletContextHandler context = new ServletContextHandler(contexts, "/", ServletContextHandler.SESSIONS);

        Filter monitoringFilter = new net.bull.javamelody.MonitoringFilter();
        FilterHolder filterHolder = new FilterHolder(monitoringFilter);
        for (Map.Entry<String, String> entry : config.entrySet()) {
            if (entry.getValue() != null && validParameter(entry.getKey())) {
                log.info("setting '{}' to '{}'", entry.getKey(), entry.getValue().toString());
                filterHolder.setInitParameter(entry.getKey(), entry.getValue().toString());
            }
        }

        context.addFilter(filterHolder, "/*", EnumSet.of(DispatcherType.INCLUDE, DispatcherType.REQUEST));

        RequestLogHandler requestLogHandler = new RequestLogHandler();
        contexts.addHandler(requestLogHandler);

        HandlerCollection handlers = new HandlerCollection();
        handlers.setHandlers(new Handler[] { contexts });
        server.setHandler(handlers);

    }

    private boolean validParameter(String key) {
        return Arrays.stream(Parameter.values()).map(p -> p.getCode()).filter(code -> code.equals(key)).findFirst()
                .isPresent();
    }

    @Deactivate
    public void deactivate() {
        try {
            server.stop();
        } catch (Exception e) {
            log.error(e.getMessage(), e);
        }

    }
}
