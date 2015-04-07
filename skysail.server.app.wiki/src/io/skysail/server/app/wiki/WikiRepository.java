package io.skysail.server.app.wiki;

import io.skysail.server.app.wiki.spaces.Space;
import io.skysail.server.db.DbRepository;
import io.skysail.server.db.DbService2;

import java.util.List;

import aQute.bnd.annotation.component.Activate;
import aQute.bnd.annotation.component.Component;
import aQute.bnd.annotation.component.Reference;

@Component(immediate = true, properties = "name=wikiRepository")
public class WikiRepository implements DbRepository {

    private static DbService2 dbService;

    @Activate
    public void activate() {
        dbService.setupVertices(Space.class.getSimpleName());
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


}
