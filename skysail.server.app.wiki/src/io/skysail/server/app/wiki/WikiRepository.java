package io.skysail.server.app.wiki;

import java.util.List;

import aQute.bnd.annotation.component.Activate;
import aQute.bnd.annotation.component.Component;
import aQute.bnd.annotation.component.Reference;
import de.twenty11.skysail.server.core.db.DbRepository;
import de.twenty11.skysail.server.core.db.DbService2;

@Component(immediate = true, properties = "name=wikiRepository")
public class WikiRepository implements DbRepository {

    private static DbService2 dbService;

    @Activate
    public void activate() {
        // dbService.setupVertices(Application.class.getSimpleName());
    }

    @Reference
    public void setDbService(DbService2 dbService) {
        WikiRepository.dbService = dbService;
    }

    public void unsetDbService(DbService2 dbService) {
        WikiRepository.dbService = null;
    }

    public <T> List<T> findAll(Class<T> cls) {
        return dbService.findObjects("select from " + cls.getSimpleName());
    }

    public static Object add(Object entity, String... edges) {
        return dbService.persist(entity, edges);
    }

    public <T> T getById(Class<?> cls, String id) {
        return dbService.findObjectById(cls, id);
    }

    // public void update(Application entity) {
    // dbService.update(entity.getId(), entity);
    // }

}
