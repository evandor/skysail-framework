package io.skysail.server.app.contacts.domain.companies;

import java.util.List;

import org.codehaus.jettison.json.JSONObject;

import aQute.bnd.annotation.component.Activate;
import aQute.bnd.annotation.component.Component;
import aQute.bnd.annotation.component.Deactivate;
import aQute.bnd.annotation.component.Reference;
import de.twenty11.skysail.server.core.db.GraphDbService;

@Component
public class CompaniesRepository {

    private static CompaniesRepository instance;

    private GraphDbService dbService;

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
    public void setDbService(GraphDbService dbService) {
        this.dbService = dbService;
    }

    public void unsetDbService(GraphDbService dbService) {
        this.dbService = null;
    }

    public Company add(Company entity) {
        Object id = dbService.persist(entity);
        return null;
    }

    public List<Company> getCompanies() {
        return null;// dbService.findAll(Company.class);
    }

    public JSONObject getById(String id) {
        return dbService.find(Company.class, id);
    }

    public List<String> getCompanysAsJson(String username) {
        return dbService.getAll(Company.class, username);
    }

    public void update(JSONObject json) {
        dbService.update(json);
    }

    // public void update(Company entity) {
    // dbService.update(entity);
    // }
    //
    // public void delete(String id) {
    // dbService.delete(id);
    // }

}