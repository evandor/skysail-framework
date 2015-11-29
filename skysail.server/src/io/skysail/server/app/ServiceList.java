package io.skysail.server.app;

import java.util.*;
import java.util.concurrent.atomic.AtomicReference;
import java.util.stream.Stream;

import org.osgi.service.cm.ConfigurationAdmin;
import org.osgi.service.component.annotations.*;
import org.osgi.service.event.EventAdmin;
import org.restlet.Context;

import de.twenty11.skysail.server.SkysailComponent;
import de.twenty11.skysail.server.app.*;
import de.twenty11.skysail.server.services.EncryptorService;
import io.skysail.api.peers.PeersProvider;
import io.skysail.api.text.*;
import io.skysail.api.um.*;
import io.skysail.api.validation.ValidatorService;
import io.skysail.server.restlet.filter.HookFilter;
import io.skysail.server.restlet.resources.SkysailServerResource;
import io.skysail.server.services.PerformanceMonitor;
import io.skysail.server.text.TranslationStoreHolder;
import lombok.Getter;
import lombok.extern.slf4j.Slf4j;

/**
 * manages the list of default services which will be injected into all
 * (currently available) skysail applications (by calling the
 * applicationsListProvider).
 *
 * <p>
 * This class connected with the {@link ApplicationList}, which keeps track of
 * all currently available skysail applications and injects the services once a
 * new application becomes available.
 * </p>
 *
 * <p>
 * Non of the references should be defined as mandatory, as, otherwise, the
 * whole ServiceList will not be available. Clients themselves have to decide
 * how to deal with the absence of services.
 * </p>
 *
 */
@Component(immediate = true)
@Slf4j
public class ServiceList implements ServiceListProvider {

    private volatile AuthorizationService authorizationService;
    private volatile AuthenticationService authenticationService;

    @Getter
    private volatile Set<TranslationRenderServiceHolder> translationRenderServices = Collections
            .synchronizedSet(new HashSet<>());

    @Getter
    private volatile Set<TranslationStoreHolder> translationStores = Collections.synchronizedSet(new HashSet<>());
    private volatile Set<HookFilter> hookFilters = Collections.synchronizedSet(new HashSet<>());
    private volatile Set<PerformanceMonitor> performanceMonitors = Collections.synchronizedSet(new HashSet<>());

    //http://stackoverflow.com/questions/30061032/best-way-to-handle-dynamic-osgi-service-dependencies
    private AtomicReference<ApplicationListProvider> applicationListProvider = new AtomicReference<>();
    private AtomicReference<ConfigurationAdmin> configurationAdmin = new AtomicReference<>();
    private AtomicReference<SkysailComponentProvider> skysailComponentProviderRef = new AtomicReference<>();
    private AtomicReference<EventAdmin> eventAdmin = new AtomicReference<>();
    private AtomicReference<EncryptorService> encryptorService = new AtomicReference<>();
    private AtomicReference<ValidatorService> validatorService = new AtomicReference<>();
    private AtomicReference<PeersProvider> peersProvider = new AtomicReference<>();

    /** === UserManagementProvider Service ============================== */

    @Reference(policy = ReferencePolicy.DYNAMIC, cardinality = ReferenceCardinality.MANDATORY)
    public synchronized void setUserManagementProvider(UserManagementProvider provider) {
        this.authenticationService = provider.getAuthenticationService();
        this.authorizationService = provider.getAuthorizationService();
    }

    public synchronized void unsetUserManagementProvider(UserManagementProvider provider) {
        this.authenticationService = null;
        this.authorizationService = null;
    }

    @Override
    public AuthenticationService getAuthenticationService() {
        return authenticationService;
    }

    @Override
    public AuthorizationService getAuthorizationService() {
        return authorizationService;
    }

    /** === ApplicationListProvider Service ============================== */

    @Reference(policy = ReferencePolicy.DYNAMIC, cardinality = ReferenceCardinality.OPTIONAL)
    public synchronized void setApplicationListProvider(ApplicationListProvider applicationProvider) {
        this.applicationListProvider.set(applicationProvider);
    }

    public void unsetApplicationListProvider(ApplicationListProvider service) {
        this.applicationListProvider.compareAndSet(service, null);
    }

    /** === ConfigAdmin Service ============================== */

    @Reference(policy = ReferencePolicy.DYNAMIC, cardinality = ReferenceCardinality.OPTIONAL)
    public synchronized void setSkysailComponentProvider(SkysailComponentProvider service) {
        skysailComponentProviderRef.set(service);
        if (skysailComponentProviderRef.get() == null) {
            log.error("skysailComponent from Provider is null!!!");
        }
        Context appContext = skysailComponentProviderRef.get().getSkysailComponent().getContext().createChildContext();
        getSkysailApps().forEach(app -> app.setContext(appContext));
        applicationListProvider.get().attach(skysailComponentProviderRef.get().getSkysailComponent());
    }

    public synchronized void unsetSkysailComponentProvider(SkysailComponentProvider service) {
        this.skysailComponentProviderRef.compareAndSet(service, null);
        getSkysailApps().forEach(a -> a.setContext(null));
        applicationListProvider.get().detach(service.getSkysailComponent());
    }

    @Override
    public SkysailComponent getSkysailComponent() {
        return skysailComponentProviderRef.get().getSkysailComponent();
    }

    /** === PeersProvider Service ============================== */

    @Reference(policy = ReferencePolicy.DYNAMIC, cardinality = ReferenceCardinality.OPTIONAL)
    public synchronized void setPeersProvider(PeersProvider service) {
        this.peersProvider.set(service);
        //getSkysailApps().forEach(app -> app.setPeersProvider(service));
    }

    public synchronized void unsetPeersProvider(PeersProvider service) {
        this.peersProvider.compareAndSet(service, null);
        //getSkysailApps().forEach(a -> a.setPeersProvider(null));
    }

    @Override
    public AtomicReference<PeersProvider> getPeersProvider() {
        return peersProvider;
    }

    /** === TranslationRenderService ============================== */

    @Reference(policy = ReferencePolicy.DYNAMIC, cardinality = ReferenceCardinality.MULTIPLE)
    public synchronized void addTranslationRenderService(TranslationRenderService service, Map<String, String> props) {
        TranslationRenderServiceHolder holder = new TranslationRenderServiceHolder(service, props);
        this.translationRenderServices.add(holder);
    }

    public synchronized void removeTranslationRenderService(TranslationRenderService service) {
        TranslationRenderServiceHolder holder = new TranslationRenderServiceHolder(service,
                new HashMap<String, String>());
        this.translationRenderServices.remove(holder);
    }

    /** === TranslationStoresService ============================== */

    @Reference(policy = ReferencePolicy.DYNAMIC, cardinality = ReferenceCardinality.MULTIPLE)
    public synchronized void addTranslationStore(TranslationStore service, Map<String, String> props) {
        TranslationStoreHolder holder = new TranslationStoreHolder(service, props);
        this.translationStores.add(holder);
    }

    public synchronized void removeTranslationStore(TranslationStore service) {
        TranslationStoreHolder holder = new TranslationStoreHolder(service, new HashMap<String, String>());
        this.translationStores.remove(holder);
    }

    /** === Encryptor Service ============================== */

    @Reference(policy = ReferencePolicy.DYNAMIC, cardinality = ReferenceCardinality.OPTIONAL)
    public synchronized void setEncryptorService(EncryptorService service) {
        this.encryptorService.set(service);
    }

    public synchronized void unsetEncryptorService(EncryptorService service) {
        this.encryptorService.compareAndSet(service, null);
    }

    @Override
    public AtomicReference<EncryptorService> getEncryptorService() {
        return encryptorService;
    }

    /** === EntityChangedHookService Service ============================== */

    // removed, there was no use case yet

    /** === EventAdmin Service ============================== */

    @Reference(policy = ReferencePolicy.DYNAMIC, cardinality = ReferenceCardinality.OPTIONAL)
    public void setEventAdminService(EventAdmin service) {
        this.eventAdmin.set(service);
    }

    public void unsetEventAdminService(EventAdmin service) {
        this.eventAdmin.compareAndSet(service, null);
    }

    @Override
    public AtomicReference<EventAdmin> getEventAdmin() {
        return eventAdmin;
    }

    /** === ConfigAdmin Service ============================== */

    @Reference(policy = ReferencePolicy.DYNAMIC, cardinality = ReferenceCardinality.OPTIONAL)
    public synchronized void setConfigurationAdminService(ConfigurationAdmin service) {
        this.configurationAdmin.set(service);
    }

    public synchronized void unsetConfigurationAdminService(ConfigurationAdmin service) {
        this.configurationAdmin.compareAndSet(service, null);
    }

    /** === Performance Monitor Service ============================== */

    @Reference(policy = ReferencePolicy.DYNAMIC, cardinality = ReferenceCardinality.MULTIPLE)
    public synchronized <R extends SkysailServerResource<T>, T> void addPerformanceMonitor(PerformanceMonitor monitor) {
        performanceMonitors.add(monitor);
//        getSkysailApps().forEach(app -> app.addMonitor(monitor));
    }

    public synchronized <R extends SkysailServerResource<T>, T> void removePerformanceMonitor(PerformanceMonitor monitor) {
        performanceMonitors.remove(monitor);
//        getSkysailApps().forEach(app -> app.removeMonitor(monitor));
    }

    @Override
    public Set<PerformanceMonitor> getPerformanceMonitors() {
        return performanceMonitors;
    }

    /** === Validation Provider ============================== */

    @Reference(policy = ReferencePolicy.DYNAMIC, cardinality = ReferenceCardinality.OPTIONAL)
    public synchronized void setValidatorService(ValidatorService service) {
        this.validatorService.set(service);
//        getSkysailApps().forEach(app -> app.setValidatorService(service));
    }

    public synchronized void unsetValidatorService(ValidatorService service) {
        this.validatorService.compareAndSet(service, null);
//        getSkysailApps().forEach(a -> a.setValidatorService(null));
    }

    @Override
    public AtomicReference<ValidatorService> getValidatorService() {
        return validatorService;
    }

    private Stream<SkysailApplication> getSkysailApps() {
        if (applicationListProvider.get() == null) {
            return Stream.empty();
        }
        return applicationListProvider.get().getApplications().stream();
    }

}
