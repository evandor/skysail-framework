package io.skysail.server.app.crm.domain.contracts;

import java.util.List;

import org.codehaus.jettison.json.JSONObject;

import aQute.bnd.annotation.component.Activate;
import aQute.bnd.annotation.component.Component;
import aQute.bnd.annotation.component.Deactivate;
import aQute.bnd.annotation.component.Reference;
import de.twenty11.skysail.server.core.db.DbService;
import de.twenty11.skysail.server.core.db.DbService2;

@Component
public class ContractsRepository {

    private static ContractsRepository instance;

    private DbService2 dbService;

    public static ContractsRepository getInstance() {
        // for tests
        if (instance == null) {
            instance = new ContractsRepository();
        }
        return instance;
    }

    @Activate
    public void activate() {
        ContractsRepository.instance = this;
    }

    @Deactivate
    public void deactivate() {
        ContractsRepository.instance = null;
    }

    @Reference
    public void setDbService(DbService2 dbService) {
        this.dbService = dbService;
    }

    public void unsetDbService(DbService dbService) {
        this.dbService = null;
    }

    // public Contract add(Contract entity) {
    // return dbService.persist(entity);
    // }
    //
    // public List<Contract> getContracts() {
    // return dbService.findAll(Contract.class);
    // }
    //
    public Contract getById(String id) {
        return dbService.findObjectById(Contract.class, id);
    }

    //
    // public void update(Contract entity) {
    // dbService.update(entity);
    // }
    //
    // public void delete(String id) {
    // dbService.delete(id);
    // }

    // public List<String> getContractsAsJson(String username) {
    // String sql = "SELECT from Contract";// WHERE owner= :username";
    // Map<String, Object> params = new HashMap<String, Object>();
    // params.put("username", username);
    // return dbService.findAndReturnJson(sql, Contract.class, params);
    //
    // }
    //
    // public long getContractsCount(String username) {
    // Map<String, Object> params = new HashMap<String, Object>();
    // params.put("username", username);
    // return dbService
    // .query(new
    // OSQLSynchQuery<ODocument>("select COUNT(*) as count from Contract WHERE owner='"
    // + username
    // + "'"), Contract.class).get(0).field("count");
    // }

    public List<String> getContracts(int page, String username, int linesPerPage, int pageSize) {
        // String sql =
        // "select * from Contract WHERE owner= :username ORDER BY lastname ASC, firstname ASC SKIP "
        // + (linesPerPage * (page - 1)) + " LIMIT " + pageSize;
        // Map<String, Object> params = new HashMap<String, Object>();
        // params.put("username", username);
        // return dbService.findAndReturnJson(sql, Contract.class, params);
        return dbService.getAll(Contract.class, username);
    }

    public Contract add(Contract entity) {
        Object id = dbService.persist(entity, "worksFor");
        return null;
    }

    public void update(JSONObject json) {
        dbService.update(json);
    }
}