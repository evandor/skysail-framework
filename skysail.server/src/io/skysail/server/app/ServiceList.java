package io.skysail.server.app;

import java.util.*;
import java.util.concurrent.atomic.AtomicReference;
import java.util.stream.Stream;

import org.osgi.service.component.annotations.*;
import org.restlet.Context;

import de.twenty11.skysail.server.SkysailComponent;
import de.twenty11.skysail.server.app.*;
import io.skysail.api.text.*;
import io.skysail.api.um.*;
import io.skysail.api.validation.ValidatorService;
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
 */
@Component(immediate = true)
@Slf4j
public class ServiceList implements ServiceListProvider {

    @Getter
    @Reference(cardinality = ReferenceCardinality.MANDATORY)
    public volatile ValidatorService validatorService;

    @Getter
    @Reference(cardinality = ReferenceCardinality.MANDATORY)
    private volatile ApplicationListProvider applicationListProvider;

    private volatile AuthorizationService authorizationService;
    private volatile AuthenticationService authenticationService;

    @Getter
    private volatile Set<TranslationRenderServiceHolder> translationRenderServices = Collections
            .synchronizedSet(new HashSet<>());

    @Getter
    private volatile Set<TranslationStoreHolder> translationStores = Collections.synchronizedSet(new HashSet<>());
    //private volatile Set<PerformanceMonitor> performanceMonitors = Collections.synchronizedSet(new HashSet<>());

    private AtomicReference<SkysailComponentProvider> skysailComponentProviderRef = new AtomicReference<>();

    
    @Getter
    @Reference(policy = ReferencePolicy.DYNAMIC, cardinality = ReferenceCardinality.MULTIPLE)
    public volatile Set<PerformanceMonitor> performanceMonitors = new HashSet<>();
        
    
    @Activate
    public void activate() {
        applicationListProvider.attach(skysailComponentProviderRef.get().getSkysailComponent());
    }

    @Deactivate
    public void deactivate() {
       // applicationListProvider.detach(service.getSkysailComponent());
    }
    
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
    public synchronized void setSkysailComponentProvider(SkysailComponentProvider service) {
        skysailComponentProviderRef.set(service);
        if (skysailComponentProviderRef.get() == null) {
            log.error("skysailComponent from Provider is null!!!");
        }
        Context appContext = skysailComponentProviderRef.get().getSkysailComponent().getContext().createChildContext();
        getSkysailApps().forEach(app -> app.setContext(appContext));
    }

    public synchronized void unsetSkysailComponentProvider(SkysailComponentProvider service) {
        this.skysailComponentProviderRef.compareAndSet(service, null);
        getSkysailApps().forEach(a -> a.setContext(null));
    }

    @Override
    public SkysailComponent getSkysailComponent() {
        return skysailComponentProviderRef.get().getSkysailComponent();
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


    private Stream<SkysailApplication> getSkysailApps() {
        if (applicationListProvider == null) {
            return Stream.empty();
        }
        return applicationListProvider.getApplications().stream();
    }

}
