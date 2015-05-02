package io.skysail.server.app;

import io.skysail.api.documentation.DocumentationProvider;
import io.skysail.api.favorites.FavoritesService;
import io.skysail.api.peers.PeersProvider;
import io.skysail.api.text.*;
import io.skysail.api.um.*;
import io.skysail.api.validation.ValidatorService;
import io.skysail.server.restlet.filter.HookFilter;
import io.skysail.server.restlet.resources.SkysailServerResource;
import io.skysail.server.services.PerformanceMonitor;
import io.skysail.server.text.TranslationStoreHolder;

import java.util.*;
import java.util.stream.Stream;

import lombok.Getter;
import lombok.extern.slf4j.Slf4j;

import org.osgi.service.cm.ConfigurationAdmin;
import org.osgi.service.event.EventAdmin;
import org.restlet.Context;

import aQute.bnd.annotation.component.*;
import de.twenty11.skysail.server.SkysailComponent;
import de.twenty11.skysail.server.app.*;
import de.twenty11.skysail.server.metrics.*;
import de.twenty11.skysail.server.services.EncryptorService;

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

    private volatile ApplicationListProvider applicationListProvider;
    private volatile AuthorizationService authorizationService;
    private volatile FavoritesService favoritesService;
    private volatile AuthenticationService authenticationService;

    @Getter
    private volatile Set<TranslationRenderServiceHolder> translationRenderServices = Collections
            .synchronizedSet(new HashSet<>());

    @Getter
    private volatile Set<TranslationStoreHolder> translationStores = Collections.synchronizedSet(new HashSet<>());

    private volatile EncryptorService encryptorService;
    private volatile EventAdmin eventAdmin;
    private volatile ConfigurationAdmin configurationAdmin;
    private volatile SkysailComponent skysailComponent;
    private volatile MetricsService metricsService;
    private volatile Set<HookFilter> hookFilters = Collections.synchronizedSet(new HashSet<>());
    private volatile ValidatorService validatorService;
    private volatile DocumentationProvider documentationProvider;
    private volatile UserManagementProvider userManagementProvider;
    private volatile Set<PerformanceMonitor> performanceMonitors = Collections.synchronizedSet(new HashSet<>());
    private volatile PeersProvider peersProvider;

    /** === UserManagementProvider Service ============================== */

    @Reference(optional = false, dynamic = true, multiple = false)
    public synchronized void setUserManagementProvider(UserManagementProvider provider) {
        this.userManagementProvider = provider;
        this.authenticationService = provider.getAuthenticationService();
        this.authorizationService = provider.getAuthorizationService();
        getSkysailApps().forEach(app -> app.setAuthenticationService(provider.getAuthenticationService()));
        getSkysailApps().forEach(app -> app.setAuthorizationService(provider.getAuthorizationService()));
    }

    public void unsetUserManagementProvider(@SuppressWarnings("unused") UserManagementProvider provider) {
        this.userManagementProvider = null;
        getSkysailApps().forEach(app -> app.setAuthorizationService(null));
        getSkysailApps().forEach(app -> app.setAuthorizationService(null));
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

    @Reference(optional = true, dynamic = true, multiple = false)
    public synchronized void setApplicationListProvider(ApplicationListProvider applicationProvider) {
        this.applicationListProvider = applicationProvider;
    }

    public void unsetApplicationListProvider(@SuppressWarnings("unused") ApplicationListProvider service) {
        this.applicationListProvider = null;
    }

    /** === ConfigAdmin Service ============================== */

    @Reference(optional = true, dynamic = true, multiple = false)
    public synchronized void setSkysailComponentProvider(SkysailComponentProvider service) {
        this.skysailComponent = service.getSkysailComponent();
        if (skysailComponent == null) {
            log.error("skysailComponent from Provider is null!!!");
        }
        Context appContext = skysailComponent.getContext().createChildContext();
        getSkysailApps().forEach(app -> app.setContext(appContext));
        applicationListProvider.attach(skysailComponent);
    }

    public synchronized void unsetSkysailComponentProvider(@SuppressWarnings("unused") SkysailComponentProvider service) {
        this.skysailComponent = null;
        getSkysailApps().forEach(a -> a.setContext(null));
        applicationListProvider.detach(skysailComponent);
    }

    @Override
    public SkysailComponent getSkysailComponent() {
        return skysailComponent;
    }

    /** === Favorites Service ============================== */

    @Reference(optional = true, dynamic = true, multiple = false)
    public synchronized void setFavoritesService(FavoritesService service) {
        this.favoritesService = service;
        getSkysailApps().forEach(app -> app.setFavoritesService(service));
    }

    public synchronized void unsetFavoritesService(@SuppressWarnings("unused") FavoritesService service) {
        getSkysailApps().forEach(a -> a.setFavoritesService(null));
    }

    @Override
    public FavoritesService getFavoritesService() {
        return favoritesService;
    }

    /** === PeersProvider Service ============================== */

    @Reference(optional = true, dynamic = true, multiple = false)
    public synchronized void setPeersProvider(PeersProvider service) {
        this.peersProvider = service;
        getSkysailApps().forEach(app -> app.setPeersProvider(service));
    }

    public synchronized void unsetPeersProvider(PeersProvider service) {
        getSkysailApps().forEach(a -> a.setPeersProvider(null));
    }

    @Override
    public PeersProvider getPeersProvider() {
        return peersProvider;
    }

    /** === TranslationRenderService ============================== */

    @Reference(optional = true, dynamic = true, multiple = true)
    public synchronized void addTranslationRenderService(TranslationRenderService service, Map<String, String> props) {
        TranslationRenderServiceHolder holder = new TranslationRenderServiceHolder(service, props);
        this.translationRenderServices.add(holder);
        getSkysailApps().forEach(app -> app.addTranslationRenderService(holder));
    }

    public synchronized void removeTranslationRenderService(TranslationRenderService service) {
        TranslationRenderServiceHolder holder = new TranslationRenderServiceHolder(service,
                new HashMap<String, String>());
        this.translationRenderServices.remove(holder);
        getSkysailApps().forEach(a -> a.removeTranslationRenderService(holder));
    }

    /** === TranslationStoresService ============================== */

    @Reference(optional = true, dynamic = true, multiple = true)
    public synchronized void addTranslationStore(TranslationStore service, Map<String, String> props) {
        TranslationStoreHolder holder = new TranslationStoreHolder(service, props);
        this.translationStores.add(holder);
        getSkysailApps().forEach(app -> {
            //log.warn("adding {} to {}", service.getClass().getSimpleName(), app.getName());
            app.addTranslationStore(holder);
        });
    }

    public synchronized void removeTranslationStore(TranslationStore service) {
        TranslationStoreHolder holder = new TranslationStoreHolder(service, new HashMap<String, String>());
        this.translationStores.remove(holder);
        getSkysailApps().forEach(a -> a.removeTranslationStore(holder));
    }

    /** === Encryptor Service ============================== */

    @Reference(optional = true, dynamic = true, multiple = false)
    public synchronized void setEncryptorService(EncryptorService service) {
        this.encryptorService = service;
        getSkysailApps().forEach(app -> app.setEncryptorService(service));
    }

    public synchronized void unsetEncryptorService(@SuppressWarnings("unused") EncryptorService service) {
        this.encryptorService = null;
        getSkysailApps().forEach(a -> a.setEncryptorService(null));
    }

    @Override
    public EncryptorService getEncryptorService() {
        return encryptorService;
    }

    /** === EntityChangedHookService Service ============================== */

    // removed, there was no use case yet

    /** === EventAdmin Service ============================== */

    @Reference(optional = true, dynamic = true, multiple = false)
    public synchronized void setEventAdminService(EventAdmin service) {
        this.eventAdmin = service;
        getSkysailApps().forEach(app -> app.setEventAdmin(service));
    }

    public synchronized void unsetEventAdminService(@SuppressWarnings("unused") EventAdmin service) {
        this.eventAdmin = null;
        getSkysailApps().forEach(a -> a.setEventAdmin(null));
    }

    @Override
    public EventAdmin getEventAdmin() {
        return eventAdmin;
    }

    /** === ConfigAdmin Service ============================== */

    @Reference(optional = true, dynamic = true, multiple = false)
    public synchronized void setConfigurationAdminService(ConfigurationAdmin service) {
        this.configurationAdmin = service;
        // getSkysailApps().forEach(app -> app.setConfigurationAdmin(service));
    }

    public synchronized void unsetConfigurationAdminService(@SuppressWarnings("unused") ConfigurationAdmin service) {
        this.configurationAdmin = null;
        // getSkysailApps().forEach(a -> a.setConfigurationAdmin(null));
    }

    @Override
    public ConfigurationAdmin getConfigurationAdmin() {
        return configurationAdmin;
    }

    /** === Metrics Service ============================== */

    @Reference(optional = true, dynamic = true, multiple = false)
    public synchronized void setMetricsService(MetricsServiceProvider provider) {
        this.metricsService = provider.getMetricsService();
        getSkysailApps().forEach(app -> app.setMetricsService(metricsService));
    }

    public synchronized void unsetMetricsService(@SuppressWarnings("unused") MetricsServiceProvider provider) {
        this.metricsService = null;
        getSkysailApps().forEach(a -> a.setMetricsService(null));
    }

    @Override
    public MetricsService getMetricsService() {
        return metricsService;
    }

    /** === Hooks Service ============================== */

    @Reference(optional = true, dynamic = true, multiple = true)
    public synchronized <R extends SkysailServerResource<T>, T> void addHookFilter(HookFilter<R, T> filter) {
        hookFilters.add(filter);
        getSkysailApps().forEach(app -> app.addFilter(filter));
    }

    public synchronized <R extends SkysailServerResource<T>, T> void removeHookFilter(HookFilter<R, T> filter) {
        hookFilters.remove(filter);
        getSkysailApps().forEach(app -> app.removeFilter(filter));
    }

    @Override
    public Set<HookFilter> getHookFilters() {
        return hookFilters;
    }

    /** === Performance Monitor Service ============================== */

    @Reference(optional = true, dynamic = true, multiple = true)
    public synchronized <R extends SkysailServerResource<T>, T> void addPerformanceMonitor(PerformanceMonitor monitor) {
        performanceMonitors.add(monitor);
        getSkysailApps().forEach(app -> app.addMonitor(monitor));
    }

    public synchronized <R extends SkysailServerResource<T>, T> void removePerformanceMonitor(PerformanceMonitor monitor) {
        performanceMonitors.remove(monitor);
        getSkysailApps().forEach(app -> app.removeMonitor(monitor));
    }

    @Override
    public Set<PerformanceMonitor> getPerformanceMonitors() {
        return performanceMonitors;
    }

    /** === Validation Provider ============================== */

    @Reference(optional = true, dynamic = true, multiple = false)
    public synchronized void setValidatorService(ValidatorService service) {
        this.validatorService = service;
        getSkysailApps().forEach(app -> app.setValidatorService(service));
    }

    public synchronized void unsetValidatorService(ValidatorService service) {
        this.validatorService = null;
        getSkysailApps().forEach(a -> a.setValidatorService(null));
    }

    @Override
    public ValidatorService getValidatorService() {
        return validatorService;
    }

    /** === Documentation Provider ============================== */

    @Reference(optional = true, dynamic = true, multiple = false)
    public synchronized void setDocumentationProvider(DocumentationProvider service) {
        this.documentationProvider = service;
        getSkysailApps().forEach(app -> app.setDocumentationProvider(service));
    }

    public synchronized void unsetDocumentationProvider(DocumentationProvider service) {
        this.documentationProvider = null;
        getSkysailApps().forEach(a -> a.setDocumentationProvider(null));
    }

    @Override
    public DocumentationProvider getDocumentationProvider() {
        return documentationProvider;
    }

    private Stream<SkysailApplication> getSkysailApps() {
        return applicationListProvider.getApplications().stream();
    }

}
