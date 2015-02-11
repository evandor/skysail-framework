package io.skysail.server.ext.apt.test.twoentities.schedules;

import java.util.List;


import aQute.bnd.annotation.component.Activate;
import aQute.bnd.annotation.component.Component;
import aQute.bnd.annotation.component.Deactivate;
import aQute.bnd.annotation.component.Reference;
import de.twenty11.skysail.server.core.db.DbService;

@Component
public class SchedulesRepository {

	private static SchedulesRepository instance;

	private DbService dbService;

	public static SchedulesRepository getInstance() {
		// for tests
		if (instance == null) {
			instance = new SchedulesRepository();
		}
	    return instance;
    }

	@Activate
	public void activate() {
		SchedulesRepository.instance = this;
	}

	@Deactivate
	public void deactivate() {
		SchedulesRepository.instance = null;
	}

	@Reference
	public void setDbService(DbService dbService) {
		this.dbService = dbService;
	}

	public void unsetDbService(@SuppressWarnings("unused") DbService dbService) {
		this.dbService = null;
	}

	public Schedule add(Schedule entity) {
		return dbService.persist(entity);
    }

	public List<Schedule> getSchedules() {
	    return dbService.findAll(Schedule.class);
    }

	public Schedule getById(String id) {
	    return dbService.find(id);
    }

	public void update(Schedule entity) {
		dbService.update(entity);
    }

    public void delete(String id) {
        dbService.delete(id);
    }

}