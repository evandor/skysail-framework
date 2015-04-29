package io.skysail.server.app.wiki.repository;

import io.skysail.server.app.wiki.pages.Page;
import io.skysail.server.app.wiki.spaces.Space;
import io.skysail.server.db.*;

import java.util.*;

import aQute.bnd.annotation.component.*;

@Component(immediate = true, properties = "name=wikiRepository")
public class WikiRepository implements DbRepository {

    private static DbService2 dbService;

    @Activate
    public void activate() {
        dbService.createWithSuperClass("V", Space.class.getSimpleName());
        dbService.register(Space.class);
        dbService.createWithSuperClass("V", Page.class.getSimpleName());
        dbService.register(Page.class);
    }

    @Reference
    public void setDbService(DbService2 dbService) {
        WikiRepository.dbService = dbService;
    }

    public void unsetDbService(DbService2 dbService) {
        WikiRepository.dbService = null;
    }

    public List<Map<String,Object>> findAll(Class<?> cls) {
        return dbService.findDocuments("select from " + cls.getSimpleName());
    }

    public static Object add(Object entity, String... edges) {
        return dbService.persist(entity, edges);
    }

    public Map<String,Object> getById(Class<?> cls, String id) {
        return dbService.findDocumentById(cls, id);
    }

    public Object getObjectById(Class<?> cls, String id) {
        return dbService.findObjectById(cls, id);
    }

    public void update(String id, Space space) {
        dbService.update(id, space);
    }

    public void update(Map<String, Object> space) {
        dbService.update(space);
    }

}
