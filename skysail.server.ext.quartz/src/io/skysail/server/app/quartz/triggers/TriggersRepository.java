package io.skysail.server.app.quartz.triggers;

import java.util.List;

import aQute.bnd.annotation.component.Activate;
import aQute.bnd.annotation.component.Component;
import aQute.bnd.annotation.component.Deactivate;
import aQute.bnd.annotation.component.Reference;
import de.twenty11.skysail.server.core.db.DbService;
import de.twenty11.skysail.server.um.domain.SkysailUser;

@Component
public class TriggersRepository {

    private DbService dbService;
    
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
    public void setDbService(DbService dbservice) {
    	this.dbService = dbservice;
    }
    
    public static TriggersRepository getInstance() {
    	return TriggersRepository.instance;
    }

    public void unsetDbService(@SuppressWarnings("unused") DbService dbservice) {
    	this.dbService = null;
    }

    public Trigger getById(String id) {
        return dbService.find(id, Trigger.class);
    }

    public String getByIdAsJson(String id) {
        return dbService.findAndReturnJson(id, Trigger.class);
    }

    public Trigger add(Trigger entity) {
        return dbService.persist(entity);
    }

    public void update(Trigger entity) {
        dbService.update(entity);
    }

    public List<Trigger> getTriggers() {
    	return dbService.findAll(Trigger.class);
    }

    public List<String> getTriggersAsJson() {
        return dbService.findAllAsJsonList(Trigger.class, SkysailUser.class);
    }

    public void delete(String id) {
        dbService.delete(id);
    }

}
