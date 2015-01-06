package de.twenty11.skysail.server.config;

import aQute.bnd.annotation.component.Component;
import aQute.bnd.annotation.component.Reference;
import de.twenty11.skysail.server.services.DatabaseSetup;

@Component(immediate = true)
public class DatabaseMigrationManager {

    private DbSetup dbSetups = new DbSetup();

    @Reference(multiple = true, optional = true, dynamic = true)
    public void addDatabaseSetup(DatabaseSetup dbSetup) {
        dbSetups.add(dbSetup);
    }

    public synchronized void removeDatabaseSetup(DatabaseSetup provider) {
        // nothing to do
    }
}