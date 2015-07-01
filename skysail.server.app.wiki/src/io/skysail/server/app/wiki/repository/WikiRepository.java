package io.skysail.server.app.wiki.repository;

import io.skysail.server.app.wiki.pages.Page;
import io.skysail.server.app.wiki.spaces.Space;
import io.skysail.server.app.wiki.versions.Version;
import io.skysail.server.db.*;
import io.skysail.server.queryfilter.Filter;

import java.util.*;

import aQute.bnd.annotation.component.*;

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
        dbService.createWithSuperClass("V", Version.class.getSimpleName());
        dbService.register(Version.class);
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
    
    public <T> List<T> findAll(Class<T> cls, Filter filter, String sorting) {
        String sql;
        if (filter != null) {
            sql = "SELECT from " + cls.getSimpleName() + " WHERE " + filter.getPreparedStatement() + " " + sorting;
            return dbService.findObjects(sql, filter.getParams());
        } else {
            sql = "SELECT from " + cls.getSimpleName() + " " + sorting;
            return dbService.findObjects(sql, Collections.emptyMap());
        }
    }
    
    public static Object add(Object entity, String... edges) {
        return dbService.persist(entity, edges);
    }

    public <T> T getById(Class<?> cls, String id) {
       return dbService.findObjectById(cls, id);
    }

    public Object getObjectById(Class<?> cls, String id) {
        return dbService.findObjectById(cls, id);
    }

    public void update(String id, Space space) {
        dbService.update(id, space);
    }

    public void update(String id, Page page) {
        dbService.update(id, page);
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

    public void delete(Class<Space> cls, String id) {
        dbService.delete(cls, id);
    }

    public List<Page> findAllPages(Filter filter) {
        String sql = "SELECT * from " + Page.class.getSimpleName() + " WHERE "+filter.getPreparedStatement()+" ORDER BY name DESC ";
//                + limitClause(pagination.getLinesPerPage(),pagination.getPage()); 
        return dbService.findObjects(sql, filter.getParams());

    }

    public List<Page> findAllPages2(Filter filter) {
        return dbService.findWithGraph("",Page.class,Collections.emptyMap());
    }

}
