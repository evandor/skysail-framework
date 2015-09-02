package de.twenty11.skysail.server.app;

import io.skysail.api.documentation.DocumentationProvider;
import io.skysail.api.favorites.FavoritesService;
import io.skysail.api.peers.PeersProvider;
import io.skysail.api.um.*;
import io.skysail.api.validation.ValidatorService;
import io.skysail.server.restlet.filter.HookFilter;
import io.skysail.server.services.PerformanceMonitor;
import io.skysail.server.text.TranslationStoreHolder;

import java.util.Set;
import java.util.concurrent.atomic.AtomicReference;

import org.osgi.service.event.EventAdmin;

import aQute.bnd.annotation.ProviderType;
import de.twenty11.skysail.server.SkysailComponent;
import de.twenty11.skysail.server.services.EncryptorService;

@ProviderType
public interface ServiceListProvider {

    AuthorizationService getAuthorizationService();

    FavoritesService getFavoritesService();

    AtomicReference<PeersProvider> getPeersProvider();

    AuthenticationService getAuthenticationService();

    AtomicReference<EncryptorService> getEncryptorService();

    AtomicReference<EventAdmin> getEventAdmin();

    SkysailComponent getSkysailComponent();

    Set<HookFilter> getHookFilters();

    AtomicReference<ValidatorService> getValidatorService();

    AtomicReference<DocumentationProvider> getDocumentationProvider();

    Set<TranslationRenderServiceHolder> getTranslationRenderServices();
    Set<TranslationStoreHolder> getTranslationStores();

    Set<PerformanceMonitor> getPerformanceMonitors();

}
