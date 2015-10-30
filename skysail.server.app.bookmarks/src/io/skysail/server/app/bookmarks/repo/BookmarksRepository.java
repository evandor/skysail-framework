package io.skysail.server.app.bookmarks.repo;

import io.skysail.api.domain.Identifiable;
import io.skysail.api.repos.DbRepository;
import io.skysail.server.app.bookmarks.Bookmark;
import io.skysail.server.db.DbService;

import java.util.*;

import org.apache.shiro.SecurityUtils;

import aQute.bnd.annotation.component.*;

@Component(immediate = true, properties = "name=BookmarksRepository")
public class BookmarksRepository implements DbRepository {

    private static DbService dbService;

    @Activate
    public void activate() {
        dbService.createWithSuperClass("V", Bookmark.class.getSimpleName());
        dbService.register(Bookmark.class);
        //dbService.createUniqueIndex(TodoList.class, "name", "owner");
    }

    @Reference
    public void setDbService(DbService dbService) {
        BookmarksRepository.dbService = dbService;
    }

    public void unsetDbService(DbService dbService) {
        BookmarksRepository.dbService = null;
    }

    public <T> List<T> findAll(Class<T> cls, String sorting) {
        String username = SecurityUtils.getSubject().getPrincipal().toString();

        String sql = "SELECT from "+cls.getSimpleName()+" WHERE owner= :username " + sorting;
        Map<String, Object> params = new HashMap<String, Object>();
        params.put("username", username);
        return dbService.findObjects(sql, params);
    }


//    public List<TodoList> findAllLists() {
//        String username = SecurityUtils.getSubject().getPrincipal().toString();
//        String sql = "SELECT from "+TodoList.class.getSimpleName()+" WHERE owner= :username ORDER BY name ";
//        Map<String, Object> params = new HashMap<String, Object>();
//        params.put("username", username);
//        return dbService.findObjects(sql, params);
//    }
//
    public static Object add(Identifiable entity, String... edges) {
        return dbService.persist(entity, edges);
    }


    public <T> T getById(Class<?> cls, String id) {
        return dbService.findObjectById(cls, id);
    }

    public Object update(String id, Identifiable entity, String... edges) {
        return dbService.update(id, entity);
    }

//    public Object add(Todo entity) {
//         Object result = dbService.persist(entity);
//         increaseOtherTodosRank(entity);
//         return result;
//    }
//
//    private void increaseOtherTodosRank(Todo entity) {
//        String sql = "update " + Todo.class.getSimpleName() + " INCREMENT rank = 1 WHERE owner = :username AND rank >= :referenceRank";
//        Map<String, Object> params = new HashMap<String, Object>();
//        params.put("username", entity.getOwner());
//        params.put("referenceRank", entity.getRank());
//        dbService.executeUpdate(sql, params);
//    }
//
    public void delete(Class<?> cls, String id) {
        dbService.delete(cls, id);
    }
//
//    public long getTodosCount(String username,String listId) {
//        String sql = "select COUNT(*) as count from " + Todo.class.getSimpleName() + " WHERE " + getWhereStatement(listId);
//        Map<String, Object> params = new HashMap<String, Object>();
//        params.put("username", username);
//        params.put("list", listId);
//        return dbService.getCount(sql, params);
//    }

    @Override
    public Object save(Identifiable identifiable) {
        return null;
    }

    @Override
    public Identifiable findOne(String id) {
        return null;
    }

    @Override
    public Class<Identifiable> getRootEntity() {
        return null;//(Class<Identifiable>) new Bookmark().getClass();
    }




}