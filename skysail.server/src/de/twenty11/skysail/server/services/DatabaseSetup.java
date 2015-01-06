package de.twenty11.skysail.server.services;

import java.util.List;

import aQute.bnd.annotation.ProviderType;

@ProviderType
public interface DatabaseSetup {

    void runMigrations();

    String getPersistenceUnitName();
    
    List<String> dependsOn();
}
