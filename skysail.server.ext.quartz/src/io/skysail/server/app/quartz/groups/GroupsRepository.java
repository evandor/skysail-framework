package io.skysail.server.app.quartz.groups;

import java.util.List;

import aQute.bnd.annotation.component.Activate;
import aQute.bnd.annotation.component.Component;
import aQute.bnd.annotation.component.Deactivate;
import aQute.bnd.annotation.component.Reference;
import de.twenty11.skysail.server.core.db.DbService;
import de.twenty11.skysail.server.um.domain.SkysailUser;

@Component
public class GroupsRepository {

    private DbService dbService;
    
	private static GroupsRepository instance;
    
    @Activate
    public void activate() {
    	GroupsRepository.instance = this;
    }

    @Deactivate
    public void deactivate() {
    	GroupsRepository.instance = null;
    }

    @Reference //(dynamic = true, multiple = false, optional = false)
    public void setDbService(DbService dbservice) {
    	this.dbService = dbservice;
    }
    
    public static GroupsRepository getInstance() {
    	return GroupsRepository.instance;
    }

    public void unsetDbService(@SuppressWarnings("unused") DbService dbservice) {
    	this.dbService = null;
    }

    public Group getById(String id) {
        return dbService.find(id, Group.class);
    }

    public String getByIdAsJson(String id) {
        return dbService.findAndReturnJson(id, Group.class);
    }

    public Group add(Group entity) {
        return dbService.persist(entity);
    }

    public void update(Group entity) {
        dbService.update(entity);
    }

    public List<Group> getGroups() {
    	return dbService.findAll(Group.class);
    }

    public List<String> getGroupsAsJson() {
        return dbService.findAllAsJsonList(Group.class, SkysailUser.class);
    }

    public void delete(String id) {
        dbService.delete(id);
    }

    public List<Group> findAll() {
        return dbService.findAll(Group.class);
    }

}
