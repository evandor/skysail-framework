package io.skysail.server.app;

import java.text.DecimalFormat;
import java.util.*;

import org.apache.commons.lang.Validate;
import org.osgi.service.component.annotations.*;
import org.osgi.service.component.annotations.Component;
import org.restlet.*;
import org.restlet.data.Protocol;

import de.twenty11.skysail.server.*;
import de.twenty11.skysail.server.app.*;
import lombok.NonNull;
import lombok.extern.slf4j.Slf4j;

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
@Slf4j
public class ApplicationList implements ApplicationListProvider { // NO_UCD (unused code)

    private volatile List<SkysailApplication> applications = new ArrayList<>();
    private SkysailRootApplication rootApplication;
    private Server riapServer = new Server(Protocol.RIAP);
    private SkysailComponent skysailComponent;

    @Reference(policy = ReferencePolicy.DYNAMIC, cardinality = ReferenceCardinality.MULTIPLE)
    public synchronized void addApplicationProvider(ApplicationProvider provider) {
        SkysailApplication application = getApplication(provider);
        application.setStatusService(new SkysailStatusService());
        applications.add(application);
        attachToComponent(application);
        log.info("(+ ApplicationModel) (#{}) with name '{}'", formatSize(applications), application.getName());
    }

    public synchronized void removeApplicationProvider(ApplicationProvider provider) {
        SkysailApplication application = getApplication(provider);
        detachFromComponent(application);
        applications.remove(application);
        log.info("(- ApplicationModel) name '{}', count is {} now", application.getName(), formatSize(applications));
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

        log.debug(" >>> unsetting ServerConfiguration");

        // TODO make nicer
        log.debug(" >>> attaching skysailApplication '{}' to defaultHost", "/" + skysailApplication.getName());
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
    
    private static String formatSize(@NonNull List<?> list) {
        return new DecimalFormat("00").format(list.size());
    }


}
