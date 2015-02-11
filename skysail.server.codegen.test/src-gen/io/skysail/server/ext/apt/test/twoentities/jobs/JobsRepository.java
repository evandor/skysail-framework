package io.skysail.server.ext.apt.test.twoentities.jobs;

import java.util.List;


import aQute.bnd.annotation.component.Activate;
import aQute.bnd.annotation.component.Component;
import aQute.bnd.annotation.component.Deactivate;
import aQute.bnd.annotation.component.Reference;
import de.twenty11.skysail.server.core.db.DbService;

@Component
public class JobsRepository {

	private static JobsRepository instance;

	private DbService dbService;

	public static JobsRepository getInstance() {
		// for tests
		if (instance == null) {
			instance = new JobsRepository();
		}
	    return instance;
    }

	@Activate
	public void activate() {
		JobsRepository.instance = this;
	}

	@Deactivate
	public void deactivate() {
		JobsRepository.instance = null;
	}

	@Reference
	public void setDbService(DbService dbService) {
		this.dbService = dbService;
	}

	public void unsetDbService(@SuppressWarnings("unused") DbService dbService) {
		this.dbService = null;
	}

	public Job add(Job entity) {
		return dbService.persist(entity);
    }

	public List<Job> getJobs() {
	    return dbService.findAll(Job.class);
    }

	public Job getById(String id) {
	    return dbService.find(id);
    }

	public void update(Job entity) {
		dbService.update(entity);
    }

    public void delete(String id) {
        dbService.delete(id);
    }

}