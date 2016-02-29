package io.skysail.server.app;

import java.util.Set;

import io.skysail.api.um.*;
import io.skysail.server.SkysailComponent;
import io.skysail.server.text.TranslationStoreHolder;

@org.osgi.annotation.versioning.ProviderType
public interface ServiceListProvider {

    AuthorizationService getAuthorizationService();
    AuthenticationService getAuthenticationService();
    Set<TranslationRenderServiceHolder> getTranslationRenderServices();
    Set<TranslationStoreHolder> getTranslationStores();
    SkysailComponent getSkysailComponent();
}
