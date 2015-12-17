package de.twenty11.skysail.server.app;

import java.util.*;

import de.twenty11.skysail.server.SkysailComponent;
import io.skysail.api.um.*;
import io.skysail.api.validation.ValidatorService;
import io.skysail.server.services.PerformanceMonitor;
import io.skysail.server.text.TranslationStoreHolder;

@org.osgi.annotation.versioning.ProviderType
public interface ServiceListProvider {

    ValidatorService getValidatorService();
    
    AuthorizationService getAuthorizationService();
    AuthenticationService getAuthenticationService();
    Set<TranslationRenderServiceHolder> getTranslationRenderServices();
    Set<TranslationStoreHolder> getTranslationStores();
    SkysailComponent getSkysailComponent();
    Collection<PerformanceMonitor> getPerformanceMonitors();

}
