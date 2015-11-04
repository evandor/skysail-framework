package io.skysail.server.app.bookmarks.repo;

import io.skysail.api.repos.DbRepository;
import io.skysail.server.app.bookmarks.Bookmark;
import io.skysail.server.db.*;
import lombok.extern.slf4j.Slf4j;
import aQute.bnd.annotation.component.*;

@Component(immediate = true, properties = "name=BookmarksRepository")
@Slf4j
public class BookmarksRepository extends GraphDbRepository<Bookmark> implements DbRepository {

    @Activate
    public void activate() {
        log.info("activating VersionsRepository");
        dbService.createWithSuperClass("V", Bookmark.class.getSimpleName());
        dbService.register(Bookmark.class);
    }

    @Reference
    public void setDbService(DbService dbService) {
        this.dbService = dbService;
    }

    public void unsetDbService(DbService dbService) {
        this.dbService = null;
    }

//    public <T> List<T> findAll(Class<T> cls, String sorting) {
//        String username = SecurityUtils.getSubject().getPrincipal().toString();
//
//        String sql = "SELECT from "+cls.getSimpleName()+" WHERE owner= :username " + sorting;
//        Map<String, Object> params = new HashMap<String, Object>();
//        params.put("username", username);
//        return dbService.findObjects(sql, params);
//    }
//
//
//    public <T> T getById(Class<?> cls, String id) {
//        return dbService.findObjectById(cls, id);
//    }
//
//    public Object update(String id, Identifiable entity, String... edges) {
//        return dbService.update(id, entity);
//    }
//
//    public void delete(Class<?> cls, String id) {
//        dbService.delete(cls, id);
//    }
//
//    @Override
//    public Object save(Identifiable identifiable) {
//        return null;
//    }
//
//    @Override
//    public Class<Identifiable> getRootEntity() {
//        return (Class<Identifiable>) new Bookmark().getClass();
//    }




}