package de.twenty11.skysail.server.app;

import io.skysail.api.validation.ValidatorService;

import java.util.List;
import java.util.Set;

import org.osgi.service.cm.ConfigurationAdmin;
import org.osgi.service.event.EventAdmin;

import aQute.bnd.annotation.ProviderType;
import de.twenty11.skysail.api.favorites.FavoritesService;
import de.twenty11.skysail.api.hooks.EntityChangedHookService;
import de.twenty11.skysail.api.security.AuthorizationService;
import de.twenty11.skysail.api.services.TranslationService;
import de.twenty11.skysail.server.SkysailComponent;
import de.twenty11.skysail.server.core.restlet.filter.HookFilter;
import de.twenty11.skysail.server.metrics.MetricsService;
import de.twenty11.skysail.server.security.AuthenticationService;
import de.twenty11.skysail.server.services.EncryptorService;
import de.twenty11.skysail.server.services.RequestResponseMonitor;

@ProviderType
public interface ServiceListProvider {

    AuthorizationService getAuthorizationService();

    FavoritesService getFavoritesService();

    AuthenticationService getAuthenticationService();

    TranslationService getTranslationService();

    EncryptorService getEncryptorService();

    List<EntityChangedHookService> getEntityChangedHookService();

    EventAdmin getEventAdmin();

    RequestResponseMonitor getRequestResponseMonitor();

    ConfigurationAdmin getConfigurationAdmin();

    SkysailComponent getSkysailComponent();

    MetricsService getMetricsService();

    Set<HookFilter> getHookFilters();

    ValidatorService getValidatorService();

}
