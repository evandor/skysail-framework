package io.skysail.server.app;

import java.util.*;
import java.util.concurrent.atomic.AtomicReference;

import org.apache.commons.lang.Validate;
import org.restlet.*;
import org.restlet.data.Protocol;
import org.slf4j.*;

import aQute.bnd.annotation.component.*;
import aQute.bnd.annotation.component.Component;
import de.twenty11.skysail.server.*;
import de.twenty11.skysail.server.app.*;

/**
 * This class keeps track of all available skysail applications and injects the
 * default services.
 * 
 * Once a new application provider is registered, it is queried for its
 * application and - if the serviceListProvider is already available - it will
 * be used to inject all default services into the new application.
 * 
 * When an application becomes unavailable, it is removed from the list again.
 * 
 * This class is connected to the @link {@link ServiceList}, which implements
 * the @link {@link ServiceListProvider} interface.
 * 
 *
 */
@Component(immediate = true)
public class ApplicationList implements ApplicationListProvider {

    private static Logger logger = LoggerFactory.getLogger(ApplicationList.class);

    private volatile List<SkysailApplication> applications = new ArrayList<>();
    private AtomicReference<ServiceListProvider> serviceListProviderRef = new AtomicReference<>();
    private SkysailRootApplication rootApplication;
    private Server riapServer = new Server(Protocol.RIAP);
    private SkysailComponent skysailComponent;

    @Reference(multiple = true, optional = true, dynamic = true)
    public synchronized void addApplicationProvider(ApplicationProvider provider) {
        SkysailApplication application = getApplication(provider);
        logger.info("(+) Adding application '{}'", application.getName());

        application.setStatusService(new SkysailStatusService());
        applications.add(application);
        attachToComponent(application);
    }

    public synchronized void removeApplicationProvider(ApplicationProvider provider) {
        SkysailApplication application = getApplication(provider);
        logger.info("(-) Removing application '{}'", application.getName());
        detachFromComponent(application);
        applications.remove(application);
    }

    /** === The service list =================================== */

    @Reference(optional = true, multiple = false, dynamic = true)
    public void setServiceListProvider(ServiceListProvider serviceListProvider) {
        this.serviceListProviderRef.set(serviceListProvider);
    }

    public void unsetServiceListProvider(ServiceListProvider serviceListProvider) {
        this.serviceListProviderRef.compareAndSet(serviceListProvider, null);
    }

    @Override
    public List<SkysailApplication> getApplications() {
        return Collections.unmodifiableList(applications);
    }

    @Override
    public void attach(SkysailComponent skysailComponent) {
        this.skysailComponent = skysailComponent;
        if (skysailComponent == null) {
            return;
        }
        getApplications().forEach(app -> attachToComponent(app));
    }

    @Override
    public void detach(SkysailComponent skysailComponent) {
        this.skysailComponent = skysailComponent;
        if (skysailComponent == null) {
            return;
        }
        getApplications().forEach(app -> detach(app, skysailComponent));
    }

    private void attachToComponent(Application application) {
        if (skysailComponent == null) {
            return;
        }
        if (application instanceof SkysailRootApplication) {
            rootApplication = (SkysailRootApplication) application;
        }
        SkysailApplication skysailApplication = (SkysailApplication) application;
        if (skysailApplication.getHome() != null) {
            // http://stackoverflow.com/questions/6810128/restlet-riap-protocol-deployed-in-java-app-server
            skysailComponent.getDefaultHost().attach("/" + skysailApplication.getHome(), application);
            skysailComponent.getInternalRouter().attach("/" + skysailApplication.getHome(), application);
            skysailComponent.getServers().add(riapServer);
        } else {
            // http://stackoverflow.com/questions/6810128/restlet-riap-protocol-deployed-in-java-app-server
            skysailComponent.getDefaultHost().attach("/" + skysailApplication.getName(), application);
            skysailComponent.getInternalRouter().attach("/" + skysailApplication.getName(), application);
        }

        if (rootApplication != null) {
            skysailComponent.getDefaultHost().attachDefault(rootApplication);
        }
    }

    private void detachFromComponent(SkysailApplication application) {
        if (skysailComponent == null) {
            return;
        }
        if (skysailComponent.getDefaultHost() != null) {
            skysailComponent.getDefaultHost().detach(application);
        }
        if (skysailComponent.getInternalRouter() != null) {
            skysailComponent.getInternalRouter().detach(application);
        }
    }

    public void detach(Application app, SkysailComponent restletComponent) {

        SkysailApplication skysailApplication = (SkysailApplication) app;

        logger.info(" >>> unsetting ServerConfiguration");

        // TODO make nicer
        logger.info(" >>> attaching skysailApplication '{}' to defaultHost", "/" + skysailApplication.getName());
        if (skysailApplication.getHome() != null) {
            // http://stackoverflow.com/questions/6810128/restlet-riap-protocol-deployed-in-java-app-server
            restletComponent.getDefaultHost().detach(app);
            restletComponent.getInternalRouter().detach(app);
            restletComponent.getServers().remove(riapServer);
        } else {
            // http://stackoverflow.com/questions/6810128/restlet-riap-protocol-deployed-in-java-app-server
            restletComponent.getDefaultHost().detach(app);
            restletComponent.getInternalRouter().detach(app);
        }
    }

    private SkysailApplication getApplication(ApplicationProvider provider) {
        SkysailApplication application = provider.getApplication();
        Validate.notNull(application, "application from applicationProvider may not be null");
        return application;
    }

}