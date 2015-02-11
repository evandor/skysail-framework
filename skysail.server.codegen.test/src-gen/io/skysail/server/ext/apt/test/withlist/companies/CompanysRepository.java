package io.skysail.server.ext.apt.test.withlist.companies;

import java.util.List;


import aQute.bnd.annotation.component.Activate;
import aQute.bnd.annotation.component.Component;
import aQute.bnd.annotation.component.Deactivate;
import aQute.bnd.annotation.component.Reference;
import de.twenty11.skysail.server.core.db.DbService;

@Component
public class CompanysRepository {

	private static CompanysRepository instance;

	private DbService dbService;

	public static CompanysRepository getInstance() {
		// for tests
		if (instance == null) {
			instance = new CompanysRepository();
		}
	    return instance;
    }

	@Activate
	public void activate() {
		CompanysRepository.instance = this;
	}

	@Deactivate
	public void deactivate() {
		CompanysRepository.instance = null;
	}

	@Reference
	public void setDbService(DbService dbService) {
		this.dbService = dbService;
	}

	public void unsetDbService(@SuppressWarnings("unused") DbService dbService) {
		this.dbService = null;
	}

	public Company add(Company entity) {
		return dbService.persist(entity);
    }

	public List<Company> getCompanys() {
	    return dbService.findAll(Company.class);
    }

	public Company getById(String id) {
	    return dbService.find(id);
    }

	public void update(Company entity) {
		dbService.update(entity);
    }

    public void delete(String id) {
        dbService.delete(id);
    }

}