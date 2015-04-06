package de.twenty11.skysail.server.app;

import io.skysail.api.documentation.DocumentationProvider;
import io.skysail.api.favorites.FavoritesService;
import io.skysail.api.um.AuthenticationService;
import io.skysail.api.um.AuthorizationService;
import io.skysail.api.validation.ValidatorService;
import io.skysail.server.restlet.filter.HookFilter;

import java.util.List;
import java.util.Set;

import org.osgi.service.cm.ConfigurationAdmin;
import org.osgi.service.event.EventAdmin;

import aQute.bnd.annotation.ProviderType;
import de.twenty11.skysail.server.SkysailComponent;
import de.twenty11.skysail.server.metrics.MetricsService;
import de.twenty11.skysail.server.services.EncryptorService;

@ProviderType
public interface ServiceListProvider {

    AuthorizationService getAuthorizationService();

    FavoritesService getFavoritesService();

    AuthenticationService getAuthenticationService();

    // TranslationService getTranslationService();

    EncryptorService getEncryptorService();

    EventAdmin getEventAdmin();

    ConfigurationAdmin getConfigurationAdmin();

    SkysailComponent getSkysailComponent();

    MetricsService getMetricsService();

    Set<HookFilter> getHookFilters();

    ValidatorService getValidatorService();

    DocumentationProvider getDocumentationProvider();

    List<TranslationRenderServiceHolder> getTranslationRenderServices();

}
