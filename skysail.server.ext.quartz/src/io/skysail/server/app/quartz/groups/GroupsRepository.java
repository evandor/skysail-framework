package io.skysail.server.app.quartz.groups;

import io.skysail.server.db.DbService2;

import java.util.List;

import aQute.bnd.annotation.component.*;

@Component
public class GroupsRepository {

    private DbService2 dbService;

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
    public void setDbService(DbService2 dbservice) {
    	this.dbService = dbservice;
    }

    public static GroupsRepository getInstance() {
    	return GroupsRepository.instance;
    }

    public void unsetDbService(@SuppressWarnings("unused") DbService2 dbservice) {
    	this.dbService = null;
    }

    public Group getById(String id) {
        return null;// dbService.find(id, Group.class);
    }

    public String getByIdAsJson(String id) {
        return null;// dbService.findAndReturnJson(id, Group.class);
    }

    public Group add(Group entity) {
        return null;// dbService.persist(entity);
    }

    public void update(Group entity) {
        //dbService.update(entity);
    }

    public List<Group> getGroups() {
    	return null;// dbService.findAll(Group.class);
    }

    public List<String> getGroupsAsJson() {
        return null;// dbService.findAllAsJsonList(Group.class, SkysailUser.class);
    }

    public void delete(String id) {
        //dbService.delete(id);
    }

    public List<Group> findAll() {
        return null;// dbService.findAll(Group.class);
    }

}
