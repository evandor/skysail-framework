package io.skysail.server.http;

import java.io.IOException;
import java.net.ServerSocket;
import java.util.*;
import java.util.logging.Level;
import java.util.stream.Collectors;

import javax.naming.ConfigurationException;

import lombok.extern.slf4j.Slf4j;

import org.osgi.service.cm.*;
import org.osgi.service.component.*;
import org.restlet.*;
import org.restlet.data.Protocol;
import org.restlet.engine.Engine;
import org.restlet.engine.converter.ConverterHelper;
import org.restlet.ext.jackson.JacksonConverter;
import org.restlet.resource.ServerResource;
import org.restlet.service.ConverterService;

import aQute.bnd.annotation.component.*;
import aQute.bnd.annotation.component.Component;
import de.twenty11.skysail.server.SkysailComponent;
import de.twenty11.skysail.server.app.*;
import de.twenty11.skysail.server.services.*;

@Component(immediate = true, configurationPolicy = ConfigurationPolicy.optional, properties = { "event.topics=de/twenty11/skysail/server/configuration/UPDATED" })
@Slf4j
public class HttpServer extends ServerResource implements RestletServicesProvider, SkysailComponentProvider,
        ManagedService, PortProvider {

    private static final String DEFAULT_PORT = "2015";

    private static Dictionary<String, ?> properties;
    private static SkysailComponent restletComponent;
    private static Server server;

    private volatile ComponentContext componentContext;
    private volatile boolean serverActive = false;
    private volatile SkysailRootApplication defaultApplication;
    private volatile List<ConverterHelper> registeredConverters;
    private volatile ConfigurationAdmin configurationAdmin;
    private volatile boolean configurationProvided = false;

    private Thread loggerThread;

    private String runningOnPort;

    public HttpServer() {
        Engine.setRestletLogLevel(Level.ALL);
        removeDefaultJacksonConverter();
        registeredConverters = Engine.getInstance().getRegisteredConverters();
    }

    /**
     * We add an overwritten implementation of the jackson converter, see @link {@link SkysailJacksonConverter}
     */
    private void removeDefaultJacksonConverter() {
        List<ConverterHelper> converters = Engine.getInstance().getRegisteredConverters().stream().filter(converter -> {
            return !(converter instanceof JacksonConverter);
        }).collect(Collectors.toList());
        Engine.getInstance().setRegisteredConverters(converters);
        
    }

    @Activate
    public synchronized void activate(ComponentContext componentContext) throws ConfigurationException {
        log.info("Activating {}", this.getClass().getName());
        this.componentContext = componentContext;
        if (restletComponent == null) {
            restletComponent = new SkysailComponent();
        }
        if (properties == null) {
            scheduleCreationOfDefaultConfiguration();
        }
    }

    @Deactivate
    protected void deactivate(ComponentContext ctxt) {
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

    // --- ConfigurationAdmin ------------------------------------------------

    @Reference(dynamic = true, optional = false, multiple = false)
    public void setConfigurationAdmin(ConfigurationAdmin configurationAdmin) {
        this.configurationAdmin = configurationAdmin;
    }

    public void unsetConfigurationAdmin(ConfigurationAdmin configurationAdmin) {
        this.configurationAdmin = null;
    }

    // --- OsgiConverterHelper
    // ------------------------------------------------------

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

    public void configurationChanged() {
        if (!serverActive) {
            return;
        }
        deactivate(componentContext);
    }

    @Override
    public ConverterService getConverterSerivce() {
        return defaultApplication.getConverterService();
    }

    @Override
    public void updated(Dictionary<String, ?> properties) {
        HttpServer.properties = properties;
        if (properties != null) {
            log.info("configuration was provided");
            configurationProvided = true;
            runningOnPort = (String) properties.get("port");
            if (!serverActive && runningOnPort != null) {
                if (runningOnPort.equals("0")) {
                    runningOnPort = findAvailablePort();
                }
                startHttpServer(runningOnPort);
            }
        }
    }

    @Override
    public String getPort() {
        return runningOnPort;
    }
    
    private String findAvailablePort() {
        ServerSocket socket = null;
        try {
            socket = new ServerSocket(0);
            return Integer.valueOf(socket.getLocalPort()).toString();
        } catch (IOException e) {
            log.error(e.getMessage(), e);
        } finally {
            if (socket != null) {
                try {
                    socket.close();
                } catch (IOException e) {
                }
            }
        }
        return null;
    }

    @Override
    public SkysailComponent getSkysailComponent() {
        return restletComponent;
    }

    private void scheduleCreationOfDefaultConfiguration() {

        Runnable runnable = () -> {
            createDefaultConfigAfterWaiting(5000);
        };
        loggerThread = new Thread(runnable);
        loggerThread.start();
    }

    private void createDefaultConfigAfterWaiting(int ms) {
        try {
            Thread.sleep(ms);

            if (configurationProvided) {
                return;
            }
            log.info("creating default configuration after waiting for {}ms for provided configuration", ms);

            Configuration config = configurationAdmin.getConfiguration(this.getClass().getName());
            Dictionary<String, Object> props = config.getProperties();
            if (props == null) {
                props = new Hashtable<String, Object>();
            }
            props.put("port", DEFAULT_PORT);
            props.put("service.pid", this.getClass().getName());
            config.update(props);
        } catch (IOException e) {
            log.error(e.getMessage(), e);
        } catch (InterruptedException e) {
            log.error(e.getMessage(), e);
        }

    }

    private void startHttpServer(String port) {
        log.info("");
        log.info("====================================");
        log.info("Starting skysail server on port {}", port);
        //System.out.println("Starting skysail server on port: " + port);
        log.info("====================================");
        log.info("");

        if (server == null) {
            try {
                server = new Server(new Context(), Protocol.HTTP, Integer.valueOf(port), restletComponent);
                server.getContext().getParameters().add("useForwardedForHeader", "true");
                //server.getContext().getParameters().add("tracing", "true");

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

}
