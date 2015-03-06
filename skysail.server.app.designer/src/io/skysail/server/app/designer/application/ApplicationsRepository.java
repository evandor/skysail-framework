package io.skysail.server.app.designer.application;

import java.util.List;

import org.codehaus.jettison.json.JSONObject;

import aQute.bnd.annotation.component.Activate;
import aQute.bnd.annotation.component.Component;
import aQute.bnd.annotation.component.Deactivate;
import aQute.bnd.annotation.component.Reference;
import de.twenty11.skysail.server.core.db.DbService;
import de.twenty11.skysail.server.core.db.DbService2;

@Component
public class ApplicationsRepository {

    private static ApplicationsRepository instance;

    private DbService2 dbService;

    public static ApplicationsRepository getInstance() {
        // for tests
        if (instance == null) {
            instance = new ApplicationsRepository();
        }
        return instance;
    }

    @Activate
    public void activate() {
        ApplicationsRepository.instance = this;
    }

    @Deactivate
    public void deactivate() {
        ApplicationsRepository.instance = null;
    }

    @Reference
    public void setDbService(DbService2 dbService) {
        this.dbService = dbService;
    }

    public void unsetDbService(DbService dbService) {
        this.dbService = null;
    }

    // public Application2 add(Application2 entity) {
    // return dbService.persist(entity);
    // }
    //
    // public List<Application2> getApplications() {
    // return dbService.findAll(Application2.class);
    // }
    //
    public Application2 getById(String id) {
        return dbService.findObjectById(Application2.class, id);
    }

    //
    // public void update(Application2 entity) {
    // dbService.update(entity);
    // }
    //
    // public void delete(String id) {
    // dbService.delete(id);
    // }

    // public List<String> getApplicationsAsJson(String username) {
    // String sql = "SELECT from Application2";// WHERE owner= :username";
    // Map<String, Object> params = new HashMap<String, Object>();
    // params.put("username", username);
    // return dbService.findAndReturnJson(sql, Application2.class, params);
    //
    // }
    //
    // public long getApplicationsCount(String username) {
    // Map<String, Object> params = new HashMap<String, Object>();
    // params.put("username", username);
    // return dbService
    // .query(new
    // OSQLSynchQuery<ODocument>("select COUNT(*) as count from Application2 WHERE owner='"
    // + username
    // + "'"), Application2.class).get(0).field("count");
    // }

    public List<String> getApplications(int page, String username, int linesPerPage, int pageSize) {
        // String sql =
        // "select * from Application2 WHERE owner= :username ORDER BY lastname ASC, firstname ASC SKIP "
        // + (linesPerPage * (page - 1)) + " LIMIT " + pageSize;
        // Map<String, Object> params = new HashMap<String, Object>();
        // params.put("username", username);
        // return dbService.findAndReturnJson(sql, Application2.class, params);
        return dbService.getAll(Application2.class, username);
    }

    public List<Application2> getApplications(String username) {
        return dbService.findObjects(Application2.class, username);
    }

    public Application2 add(Application2 entity) {
        Object id = dbService.persist(entity);
        return null;
    }

    public void update(JSONObject json) {
        dbService.update(json);
    }

    public List<Application2> getApplications() {
        return dbService.findObjects(Application2.class, "username");
    }
}