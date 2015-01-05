package de.twenty11.skysail.api.config;

import java.util.Set;

import aQute.bnd.annotation.ProviderType;

/**
 * A configurationProvider provides (configuration) values for keys, if existent.
 * 
 * Implementors might use different strategies to find the configuration values (utilizing files, system properties,
 * staging directories, etc, or combinations of those).
 * 
 * If a configuration provider is published as an OSGi service (from its containing bundle), the class
 * ServerConfigurationImpl in the skysail.server bundle will be able to utilize the configuration information.
 * 
 */
@ProviderType
public interface ConfigurationProvider {

    /**
     * @return the name of the configurationProvider for debug purposes.
     */
    String getName();

    /**
     * Returns the configuration value for a given key if existent.
     * 
     * @param key
     *            key to query for
     * @return the value for the given key or null
     */
    String getConfigForKey(String key);

    /**
     * For example,
     * 
     * peers.test.host = http://85.25.22.126 peers.test.port = 8015 peers.production.host = http://85.25.22.126
     * 
     * would be mapped to
     * 
     * "test" -> Object ("host" -> "...", "port" = "8015") "production -> Object ("host" -> "...");
     * 
     * 
     * @param string
     * @return
     */
    Config getConfig(String path);

    /**
     * @return get all keys
     */
    Set<String> getKeys();

}
