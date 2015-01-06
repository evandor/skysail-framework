package de.twenty11.skysail.server.services;

import org.restlet.service.ConverterService;

import aQute.bnd.annotation.ProviderType;

@ProviderType
public interface RestletServicesProvider {

    ConverterService getConverterSerivce();
}
