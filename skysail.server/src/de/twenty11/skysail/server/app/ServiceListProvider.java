package de.twenty11.skysail.server.app;

import java.util.Set;
import java.util.concurrent.atomic.AtomicReference;

import org.osgi.service.event.EventAdmin;

import de.twenty11.skysail.server.SkysailComponent;
import de.twenty11.skysail.server.services.EncryptorService;
import io.skysail.api.peers.PeersProvider;
import io.skysail.api.um.*;
import io.skysail.api.validation.ValidatorService;
import io.skysail.server.services.PerformanceMonitor;
import io.skysail.server.text.TranslationStoreHolder;

@org.osgi.annotation.versioning.ProviderType
public interface ServiceListProvider {

    AuthorizationService getAuthorizationService();

    AtomicReference<PeersProvider> getPeersProvider();

    AuthenticationService getAuthenticationService();

    AtomicReference<EncryptorService> getEncryptorService();

    AtomicReference<EventAdmin> getEventAdmin();

    SkysailComponent getSkysailComponent();

    AtomicReference<ValidatorService> getValidatorService();

    Set<TranslationRenderServiceHolder> getTranslationRenderServices();
    Set<TranslationStoreHolder> getTranslationStores();

    Set<PerformanceMonitor> getPerformanceMonitors();

}
