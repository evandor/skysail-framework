package de.twenty11.skysail.server.services;

import java.util.List;
import java.util.ResourceBundle;

import aQute.bnd.annotation.ProviderType;

@ProviderType
public interface ResourceBundleProvider {

    List<ResourceBundle> getResourceBundles();

}
