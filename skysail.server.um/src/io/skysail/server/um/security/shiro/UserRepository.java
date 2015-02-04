package io.skysail.server.um.security.shiro;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import de.twenty11.skysail.server.core.db.DbService;
import de.twenty11.skysail.server.um.domain.SkysailUser;

public class UserRepository {

    private DbService dbService;

    public UserRepository(DbService service) {
        dbService = service;
    }

    public SkysailUser getById(String id) {
        return dbService.find(id);
    }

    public SkysailUser getByName(String username) {
        String sql = "SELECT from SkysailUser WHERE username = :username";
        Map<String, Object> params = new HashMap<String, Object>();
        params.put("username", username);
        List<SkysailUser> findAll = dbService.findAll(sql, SkysailUser.class, params);
        if (findAll.size() == 0) {
            return null;
        }
        if (findAll.size() > 1) {
            throw new IllegalStateException("found " + findAll.size() + " entries with username " + username
                    + " in SkysailUsers");
        }
        return findAll.get(0);
    }

    public List<SkysailUser> getEntities() {
        return dbService.findAll(SkysailUser.class);
    }

    public void add(SkysailUser entity) {
        dbService.persist(entity);
    }

    // public SkysailGroup getGroups(String id) {
    // TypedQuery<SkysailGroup> query = getEntityManager().createQuery(
    // "SELECT c FROM SkysailGroup c WHERE c.pid = :pid", SkysailGroup.class);
    // // query.setParameter("pid", id);
    // return query.getSingleResult();
    // }

    // public List<SkysailGroup> getGroupsForUser(String username) {
    // TypedQuery<SkysailGroup> query =
    // getEntityManager().createQuery("SELECT c FROM SkysailGroup c",
    // SkysailGroup.class);
    // return query.getResultList();
    // }

    public void update(SkysailUser entity) {
        dbService.update(entity);
    }

    public void delete(String id) {
        dbService.delete(id);
    }

}
