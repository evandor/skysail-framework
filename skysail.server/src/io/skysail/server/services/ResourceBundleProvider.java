package io.skysail.server.services;

import java.util.*;

import org.osgi.annotation.versioning.ProviderType;

@ProviderType
public interface ResourceBundleProvider {

    List<ResourceBundle> getResourceBundles();

}
