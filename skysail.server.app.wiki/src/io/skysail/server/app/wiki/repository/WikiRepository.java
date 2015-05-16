package io.skysail.server.app.wiki.repository;

import io.skysail.server.app.wiki.pages.Page;
import io.skysail.server.app.wiki.spaces.Space;
import io.skysail.server.db.DbRepository;
import io.skysail.server.db.DbService2;

import java.util.List;
import java.util.Map;

import aQute.bnd.annotation.component.Activate;
import aQute.bnd.annotation.component.Component;
import aQute.bnd.annotation.component.Reference;

import com.orientechnologies.orient.core.record.impl.ODocument;

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
    
//    public List<Map<String,Object>> findPagesForSpace(String spaceId) {
//        
//        String sql = "SELECT from "+Space.class.getSimpleName()+" WHERE owner= :username " + sorting;
//        Map<String, Object> params = new HashMap<String, Object>();
//        params.put("username", username);
//        return dbService.findObjects(sql, params);
//        
//        return dbService.findDocuments("select from " + Page.class.getSimpleName() + " WHERE ");
//    }

    public static Object add(Object entity, String... edges) {
        return dbService.persist(entity, edges);
    }

    public ODocument getById(Class<?> cls, String id) {
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

    public void updateDocument(ODocument doc) {
        dbService.update(doc);
        
    }

    public Space getSpaceById(String id) {
        return (Space) dbService.findObjectById(Space.class, id);
    }
    
    public Page getPageById(String id) {
        return (Page) dbService.findObjectById(Page.class, id);
    }

}
