package io.skysail.api.documentation;

import java.util.Map;

import org.restlet.resource.ServerResource;

import aQute.bnd.annotation.ProviderType;

@ProviderType
public interface DocumentationProvider {

    Map<String, Class<? extends ServerResource>> getResourceMap();

}
