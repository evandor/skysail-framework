package io.skysail.server.app.quartz.triggers;

import io.skysail.server.db.DbService2;

import java.util.List;

import aQute.bnd.annotation.component.*;

@Component
public class TriggersRepository {

    private DbService2 dbService;

	private static TriggersRepository instance;

    @Activate
    public void activate() {
    	TriggersRepository.instance = this;
    }

    @Deactivate
    public void deactivate() {
    	TriggersRepository.instance = null;
    }

    @Reference //(dynamic = true, multiple = false, optional = false)
    public void setDbService(DbService2 dbservice) {
    	this.dbService = dbservice;
    }

    public static TriggersRepository getInstance() {
    	return TriggersRepository.instance;
    }

    public void unsetDbService(@SuppressWarnings("unused") DbService2 dbservice) {
    	this.dbService = null;
    }

    public Trigger getById(String id) {
        return null;// dbService.find(id, Trigger.class);
    }

    public String getByIdAsJson(String id) {
        return null;// dbService.findAndReturnJson(id, Trigger.class);
    }

    public Trigger add(Trigger entity) {
        return null;// dbService.persist(entity);
    }

    public void update(Trigger entity) {
        //dbService.update(entity);
    }

    public List<Trigger> getTriggers() {
    	return null;// dbService.findAll(Trigger.class);
    }

    public List<String> getTriggersAsJson() {
        return null;// dbService.findAllAsJsonList(Trigger.class, SkysailUser.class);
    }

    public void delete(String id) {
       //  dbService.delete(id);
    }

}
