package io.skysail.server.http;

import java.util.Dictionary;
import java.util.List;
import java.util.logging.Level;

import javax.naming.ConfigurationException;

import lombok.extern.slf4j.Slf4j;

import org.osgi.service.cm.ManagedService;
import org.osgi.service.component.ComponentContext;
import org.osgi.service.component.ComponentException;
import org.restlet.Context;
import org.restlet.Server;
import org.restlet.data.Protocol;
import org.restlet.engine.Engine;
import org.restlet.engine.converter.ConverterHelper;
import org.restlet.resource.ServerResource;
import org.restlet.service.ConverterService;

import aQute.bnd.annotation.component.Activate;
import aQute.bnd.annotation.component.Component;
import aQute.bnd.annotation.component.Deactivate;
import aQute.bnd.annotation.component.Reference;
import de.twenty11.skysail.server.SkysailComponent;
import de.twenty11.skysail.server.app.SkysailComponentProvider;
import de.twenty11.skysail.server.app.SkysailRootApplication;
import de.twenty11.skysail.server.services.OsgiConverterHelper;
import de.twenty11.skysail.server.services.RestletServicesProvider;

@Component(immediate = true, properties = { "service.pid=server",
        "event.topics=de/twenty11/skysail/server/configuration/UPDATED" })
@Slf4j
public class HttpServer extends ServerResource implements RestletServicesProvider, SkysailComponentProvider,
        ManagedService {

    private static SkysailComponent restletComponent;
    private volatile ComponentContext componentContext;
    private boolean serverActive = false;
    private volatile SkysailRootApplication defaultApplication;
    private static Server server;
    private volatile List<ConverterHelper> registeredConverters = Engine.getInstance().getRegisteredConverters();
    // private volatile SwarmClient swarmClient;
    private Dictionary<String, ?> properties;

    public HttpServer() {
        Engine.setRestletLogLevel(Level.ALL);
        // Engine.setLogLevel(Level.ALL);
    }

    @Override
    public SkysailComponent getSkysailComponent() {
        return restletComponent;
    }

    @Activate
    public synchronized void activate(ComponentContext componentContext) throws ConfigurationException {
        log.info("Activating {}", this.getClass().getName());
        this.componentContext = componentContext;
        if (restletComponent == null) {
            restletComponent = new SkysailComponent();
        }
        if (properties == null) {
            return;
        }
        String port = (String) properties.get("port");
        if (!serverActive && port != null) {
            startHttpServer(port);
        }
    }

    @Deactivate
    protected void deactivate(@SuppressWarnings("unused") ComponentContext ctxt) {
        log.info("Deactivating {}", this.getClass().getName());

        serverActive = false;
        try {
            if (server != null) {
                server.stop();
            }
        } catch (Exception e) {
            log.error("Exception when trying to stop standalone server", e);
        }

        restletComponent.getDefaultHost().detach(defaultApplication);

        restletComponent.setServers(null);

        try {
            restletComponent.stop();
        } catch (Exception e) {
            log.error(e.getMessage(), e);
        }

        this.componentContext = null;
    }

    private void startHttpServer(String port) {
        // determinePort();

        log.info("");
        log.info("====================================");
        log.info("Starting skysail server on port {}", port);
        log.info("====================================");
        log.info("");

        if (server == null) {
            try {
                server = new Server(new Context(), Protocol.HTTP, Integer.valueOf(port), restletComponent);
                server.getContext().getParameters().add("useForwardedForHeader", "true");

                server.start();
            } catch (Exception e) {
                log.error("Exception when starting standalone server trying to parse provided port (" + port + ")", e);
            }
        } else {
            try {
                server.start();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        if (server == null) {
            throw new ComponentException("skysail server could not be started");
        }

        serverActive = true;

    }

    @Override
    public ConverterService getConverterSerivce() {
        return defaultApplication.getConverterService();
    }

    @Reference(optional = true, dynamic = true, multiple = true)
    public synchronized void addConverterHelper(OsgiConverterHelper converterHelper) {
        if (converterHelper instanceof ConverterHelper) {
            this.registeredConverters.add((ConverterHelper) converterHelper);
        }
    }

    public synchronized void removeConverterHelper(OsgiConverterHelper converterHelper) {
        if (converterHelper instanceof ConverterHelper) {
            this.registeredConverters.remove(converterHelper);
        }
    }

    public boolean getServerActive() {
        return serverActive;
    }

    public void configurationChanged() {
        if (!serverActive) {
            return;
        }
        deactivate(componentContext);
    }

    // private void determinePort() {
    // if (serverConfig != null) {
    // port = serverConfig.getConfigForKey("port");
    // log.info("port was configured on {}", port);
    // } else {
    // log.info("port could not be determined from serverConfig, reusing value '{}'",
    // port);
    // }
    // }

    @Override
    public void updated(Dictionary<String, ?> properties) {
        this.properties = properties;
        if (properties == null) {
            return;
        }
        startHttpServer((String) properties.get("port"));
    }

}
