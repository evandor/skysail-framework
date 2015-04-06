package io.skysail.server.app;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.function.Consumer;

import org.apache.commons.lang.Validate;
import org.restlet.Application;
import org.restlet.Server;
import org.restlet.data.Protocol;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import aQute.bnd.annotation.component.Component;
import aQute.bnd.annotation.component.Reference;
import de.twenty11.skysail.server.SkysailComponent;
import de.twenty11.skysail.server.SkysailStatusService;
import de.twenty11.skysail.server.app.ApplicationListProvider;
import de.twenty11.skysail.server.app.ApplicationProvider;
import de.twenty11.skysail.server.app.ServiceListProvider;
import de.twenty11.skysail.server.app.SkysailRootApplication;

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
    private volatile ServiceListProvider services;
    private SkysailRootApplication rootApplication;
    private Server riapServer = new Server(Protocol.RIAP);
    private SkysailComponent skysailComponent;

    @Reference(multiple = true, optional = true, dynamic = true)
    public synchronized void addApplicationProvider(ApplicationProvider provider) {
        SkysailApplication application = getApplication(provider);
        logger.info("(+) Adding application '{}'", application.getName());

        application.setStatusService(new SkysailStatusService());
        applications.add(application);
        setServices(Arrays.asList(application));
        attachToComponent(application);
    }

    public synchronized void removeApplicationProvider(ApplicationProvider provider) {
        SkysailApplication application = getApplication(provider);
        logger.info("(-) Removing application '{}'", application.getName());
        detachFromComponent(application);
        // unassignServices(application);
        applications.remove(application);
    }

    /** === The service list =================================== */

    @Reference(optional = true, multiple = false, dynamic = true)
    public void setServiceListProvider(ServiceListProvider serviceListProvider) {
        this.services = serviceListProvider;
        setServices(applications);
    }

    public void unsetServiceListProvider(ServiceListProvider serviceListProvider) {
        this.services = null;
        unsetServices(applications);
    }

    @Override
    public List<SkysailApplication> getApplications() {
        return Collections.unmodifiableList(applications);
    }

    private void setServices(List<SkysailApplication> apps) {
        if (services == null) {
            return;
        }
        assignService(apps, app -> app.setAuthenticationService(services.getAuthenticationService()));
        assignService(apps, app -> app.setAuthorizationService(services.getAuthorizationService()));
        assignService(apps, app -> app.setFavoritesService(services.getFavoritesService()));
        // assignService(apps, app ->
        // app.setTranslationService(services.getTranslationService()));
        assignService(apps, app -> app.setEventAdmin(services.getEventAdmin()));
        assignService(apps, app -> app.setMetricsService(services.getMetricsService()));
        assignService(apps, app -> app.setValidatorService(services.getValidatorService()));
        assignService(apps, app -> app.setDocumentationProvider(services.getDocumentationProvider()));
        assignService(apps, app -> app.setTranslationRenderServices(services.getTranslationRenderServices()));
        assignService(apps, app -> app.setFilters(services.getHookFilters()));
    }

    private synchronized void unsetServices(List<SkysailApplication> apps) {
        apps.stream().forEach(app -> app.setAuthenticationService(null));
        apps.stream().forEach(app -> app.setAuthorizationService(null));
        apps.stream().forEach(app -> app.setFavoritesService(null));
        // apps.stream().forEach(app -> app.setTranslationService(null));
        apps.stream().forEach(app -> app.setEventAdmin(null));
        apps.stream().forEach(app -> app.setMetricsService(null));
        apps.stream().forEach(app -> app.setValidatorService(null));
        apps.stream().forEach(app -> app.setDocumentationProvider(null));
        apps.stream().forEach(app -> app.setTranslationRenderServices(new ArrayList<>()));
        apps.stream().forEach(app -> app.setFilters(new HashSet<>()));
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
        skysailComponent.getDefaultHost().detach(application);
        skysailComponent.getInternalRouter().detach(application);
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

    private void assignService(List<SkysailApplication> applications, Consumer<? super SkysailApplication> consumer) {
        applications.stream().forEach(consumer);
    }

    private SkysailApplication getApplication(ApplicationProvider provider) {
        SkysailApplication application = provider.getApplication();
        Validate.notNull(application, "application from applicationProvider may not be null");
        return application;
    }

}
