//package de.twenty11.skysail.server.config;
//
//import java.util.Collections;
//import java.util.Map;
//import java.util.Set;
//import java.util.concurrent.ConcurrentHashMap;
//
//import org.slf4j.Logger;
//import org.slf4j.LoggerFactory;
//
//import de.twenty11.skysail.server.services.DatabaseSetup;
//
//public class DbSetup {
//    
//    private static final Logger logger = LoggerFactory.getLogger(DbSetup.class);
//
//    private Map<String, DatabaseSetup> setups = new ConcurrentHashMap<>();
//    
//    //http://stackoverflow.com/questions/6992608/why-there-is-no-concurrenthashset-against-concurrenthashmap
//    private volatile Set<String> unprocessedPersistenceUnits = Collections.newSetFromMap(new ConcurrentHashMap<String, Boolean>());
//    private volatile Set<String> processedPersistenceUnits = Collections.newSetFromMap(new ConcurrentHashMap<String, Boolean>());
//    private volatile Set<String> processingPersistenceUnits = Collections.newSetFromMap(new ConcurrentHashMap<String, Boolean>());
//
//    public void add(DatabaseSetup dbSetup) {
//        logger.info("adding dbSetup for PU '{}'", dbSetup.getPersistenceUnitName());
//        setups.put(dbSetup.getPersistenceUnitName(), dbSetup);
//        unprocessedPersistenceUnits.add(dbSetup.getPersistenceUnitName());
//        process();
//    }
//
//    private void process() {
//        unprocessedPersistenceUnits.stream().forEach(pu -> checkDbSetup(setups.get(pu)));
//    }
//
//    private  void checkDbSetup(DatabaseSetup setup) {
//        if (processingPersistenceUnits.contains(setup.getPersistenceUnitName())) {
//            logger.info("will not migrate PU '{}' as it is already being migrated", setup.getPersistenceUnitName());
//            return;
//        }
//        if (setup.dependsOn().size() == 0) {
//            logger.info("migrating PU {} as it has no dependencies", setup.getPersistenceUnitName());
//            migrate(setup);
//            return;
//        }
//        boolean process = true;
//        for (String puName : setup.dependsOn()) {
//            if (!processedPersistenceUnits.contains(puName)) {
//                logger.info("will not migrate PU '{}' (yet) as it is missing dependency {}", setup.getPersistenceUnitName(), puName);
//                process = false;
//            }
//        }
//        if (process) {
//            logger.info("migrating PU {} as all dependencies ({}) are already migrated", setup.getPersistenceUnitName(), setup.dependsOn());
//            migrate(setup);
//        }
//    }
//
//    private synchronized void migrate(DatabaseSetup setup) {
//        unprocessedPersistenceUnits.remove(setup.getPersistenceUnitName());
//        processingPersistenceUnits.add(setup.getPersistenceUnitName());
//        logger.info("running migration of PU '{}'", setup.getPersistenceUnitName());
//        setup.runMigrations();
//        processingPersistenceUnits.remove(setup.getPersistenceUnitName());
//        processedPersistenceUnits.add(setup.getPersistenceUnitName());
//        logger.info("processed Persistence Units now are: '{}'", processedPersistenceUnits);
//        process();
//    }
//
//}
