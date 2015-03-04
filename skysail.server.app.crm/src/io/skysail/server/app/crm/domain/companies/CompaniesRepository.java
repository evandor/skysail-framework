package io.skysail.server.app.crm.domain.companies;

import java.util.List;
import java.util.Map;

import org.apache.shiro.SecurityUtils;
import org.codehaus.jettison.json.JSONObject;

import aQute.bnd.annotation.component.Activate;
import aQute.bnd.annotation.component.Component;
import aQute.bnd.annotation.component.Deactivate;
import aQute.bnd.annotation.component.Reference;
import de.twenty11.skysail.server.core.db.DbService2;

@Component
public class CompaniesRepository {

    private static CompaniesRepository instance;

    private DbService2 dbService;

    public static CompaniesRepository getInstance() {
        // for tests
        if (instance == null) {
            instance = new CompaniesRepository();
        }
        return instance;
    }

    @Activate
    public void activate() {
        CompaniesRepository.instance = this;
    }

    @Deactivate
    public void deactivate() {
        CompaniesRepository.instance = null;
    }

    @Reference
    public void setDbService(DbService2 dbService) {
        this.dbService = dbService;
    }

    public void unsetDbService(DbService2 dbService) {
        this.dbService = null;
    }

    public Company add(Company entity) {
        Object id = dbService.persist(entity);
        return null;
    }

    public Company getById(String id) {
        return dbService.findObjectById(Company.class, id);
    }

    public List<String> getCompanysAsJson(String username) {
        return dbService.getAll(Company.class, username);
    }

    public List<Map<String, Object>> getCompanys() {
        String username = SecurityUtils.getSubject().getPrincipal().toString();
        return dbService.getAllAsMap(Company.class, username);
    }

    public void update(JSONObject json) {
        dbService.update(json);
    }

}