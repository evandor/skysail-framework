package io.skysail.server.services;

import org.osgi.annotation.versioning.ProviderType;
import org.restlet.service.ConverterService;

@ProviderType
public interface RestletServicesProvider {

    ConverterService getConverterSerivce();
}
