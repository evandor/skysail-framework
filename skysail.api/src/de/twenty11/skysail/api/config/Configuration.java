package de.twenty11.skysail.api.config;

import java.net.MalformedURLException;
import java.net.URL;

import aQute.bnd.annotation.component.Component;
import aQute.bnd.annotation.component.Reference;
import de.twenty11.skysail.api.internal.ApiConstants;

@Component
public class Configuration {

    private static final String DEFAULT_HOME = "";

    private static volatile ConfigurationProvider configurationProvider;

    @Reference
    public void setConfigurationProvider(ConfigurationProvider provider) {
        configurationProvider = provider;
    }

    public void unsetConfigurationProvider(@SuppressWarnings("unused") ConfigurationProvider provider) {
        configurationProvider = null;
    }

    /**
     * home should be configured with identifier ApiConstants.SKYSAIL_PRODUCT_HOME.
     * 
     * @return home url (e.g. http://localhost:2016) without trailing slash if configured, empty string otherwise.
     */
    public static String getHome() {
        if (configurationProvider != null) {
            return getHomeFromConfigProvider();
        }
        return DEFAULT_HOME;
    }

    private static String getHomeFromConfigProvider() {
        String home = configurationProvider.getConfigForKey(ApiConstants.SKYSAIL_PRODUCT_HOME);
        if (home != null && home.trim().length() > 0) {
            checkIfUrl(home);
            return home.endsWith("/") ? home.substring(0, home.length() - 1) : home;
        }
        return DEFAULT_HOME;
    }

    private static void checkIfUrl(String home) {
        try {
            new URL(home);
        } catch (MalformedURLException e) {
            throw new IllegalArgumentException("'" + home + "' is not a valid url", e);
        }
    }

}
