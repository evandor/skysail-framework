package de.twenty11.skysail.server.mgt;

import java.lang.instrument.Instrumentation;
import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;

import org.osgi.service.cm.ConfigurationException;
import org.osgi.service.component.ComponentContext;
import org.osgi.service.component.annotations.*;
import org.osgi.service.event.EventAdmin;
import org.restlet.data.MediaType;

import de.twenty11.skysail.server.app.ApplicationProvider;
import de.twenty11.skysail.server.core.restlet.*;
import de.twenty11.skysail.server.mgt.apps.*;
import de.twenty11.skysail.server.mgt.captures.RequestCaptureResource;
import de.twenty11.skysail.server.mgt.events.EventsResource;
import de.twenty11.skysail.server.mgt.jmx.JmxMonitor;
import de.twenty11.skysail.server.mgt.performance.PerformanceResource;
import etm.core.configuration.*;
import etm.core.monitor.EtmMonitor;
import io.skysail.server.app.SkysailApplication;
import io.skysail.server.menus.MenuItemProvider;
import lombok.Getter;


@Component
public class ManagementApplication extends SkysailApplication implements ApplicationProvider, MenuItemProvider {

    public static final String APP_NAME = "management";

    private JmxMonitor jmxMonitor;

    private Instrumentation instrumentation;

	private MediaType eventStream;

	private EtmMonitor monitor;

	@Reference(cardinality = ReferenceCardinality.OPTIONAL)
	@Getter
	private volatile EventAdmin eventAdmin;
	
	@Getter
    private volatile List<ApplicationProvider> applicationProviders = new CopyOnWriteArrayList<>();

    public ManagementApplication() {
        super(APP_NAME);
        setDescription("RESTful skysail.server.management bundle");
        setOwner("twentyeleven");
        setSecuredByRoles("admin");
        setName(APP_NAME);
        addToAppContext(ApplicationContextId.IMG, "/static/img/silk/server.png");
    }

    @Activate
    public void startJmxMonitor(ComponentContext componentContext) throws ConfigurationException {
        activate(componentContext);
        jmxMonitor = new JmxMonitor();
        jmxMonitor.start();
        if (instrumentation != null) {
           // Agent.premain("", instrumentation);
        }
        BasicEtmConfigurator.configure();
        monitor = EtmManager.getEtmMonitor();
        monitor.start();

//        performanceMonitorServer = new HttpConsoleServer(monitor);
//        performanceMonitorServer.setListenPort(2017);
//        performanceMonitorServer.start();
    }

    @Deactivate
    public void stopJmxMonitor(ComponentContext componentContext) {
        deactivate(componentContext);
        jmxMonitor.stop();
        jmxMonitor = null;
        monitor.stop();
//        performanceMonitorServer.stop();
    }

    @Reference(policy = ReferencePolicy.DYNAMIC, cardinality = ReferenceCardinality.MULTIPLE)
    public void setTodoRepository(ApplicationProvider application) {
        applicationProviders.add(application);
    }

    public void unsetTodoRepository(ApplicationProvider application) {
        applicationProviders.remove(application);
    }

    @Override
    protected void attach() {
        router.setAuthorizationDefaults(anyOf("admin"));

        router.attach(new RouteBuilder("", PerformanceResource.class));

//        router.attach(new RouteBuilder("", PeersResource.class).authorizeWith(anyOf("admin")));
//        router.attach(new RouteBuilder("/", PeersResource.class).authorizeWith(anyOf("admin")));
//        router.attach(new RouteBuilder("/log", LogResource.class));
        router.attach(new RouteBuilder("/events", EventsResource.class));
        router.attach(new RouteBuilder("/captures/request", RequestCaptureResource.class));
//        router.attach(new RouteBuilder("/requests/{id}", RequestResource.class));
//        router.attach(new RouteBuilder("/responses/{id}", ResponseResource.class));
//        router.attach(new RouteBuilder("/status/heap", HeapStatsResource.class));
//        router.attach(new RouteBuilder("/serverLoad", ServerLoadResource.class));
//        router.attach(new RouteBuilder("/serverTime", ServerTimeResource.class));
        router.attach(new RouteBuilder("/applications", ApplicationsResource.class));
        router.attach(new RouteBuilder("/applications/{id}", ApplicationResource.class));
        router.attach(new RouteBuilder("/performance", PerformanceResource.class));

    }

    @Reference(policy = ReferencePolicy.DYNAMIC, cardinality = ReferenceCardinality.OPTIONAL)
    public void setInstrumentation(Instrumentation instrumentation) {
        this.instrumentation = instrumentation;
        //Agent.premain("", instrumentation);
    }

    public void unsetInstrumentation(Instrumentation instrumentation) {
        this.instrumentation = null;
    }

    public JmxMonitor getJmxMonitor() {
        return jmxMonitor;
    }

	public MediaType getEventStreamMediaType() {
	    return eventStream;
    }

	public EtmMonitor getMonitor() {
	    return monitor;
    }


}
