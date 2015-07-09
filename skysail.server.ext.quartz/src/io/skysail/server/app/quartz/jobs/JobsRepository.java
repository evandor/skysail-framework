package io.skysail.server.app.quartz.jobs;

import java.util.List;

import aQute.bnd.annotation.component.Activate;
import aQute.bnd.annotation.component.Component;
import aQute.bnd.annotation.component.Deactivate;
import aQute.bnd.annotation.component.Reference;
import de.twenty11.skysail.server.core.db.DbService;
import de.twenty11.skysail.server.um.domain.SkysailUser;

@Component
public class JobsRepository {

    private DbService dbService;
    
	private static JobsRepository instance;
    
    @Activate
    public void activate() {
    	JobsRepository.instance = this;
    }

    @Deactivate
    public void deactivate() {
    	JobsRepository.instance = null;
    }

    @Reference //(dynamic = true, multiple = false, optional = false)
    public void setDbService(DbService dbservice) {
    	this.dbService = dbservice;
    }
    
    public static JobsRepository getInstance() {
    	return JobsRepository.instance;
    }

    public void unsetDbService(@SuppressWarnings("unused") DbService dbservice) {
    	this.dbService = null;
    }

    public Job getById(String id) {
        return dbService.find(id, Job.class);
    }

    public String getByIdAsJson(String id) {
        return dbService.findAndReturnJson(id, Job.class);
    }

    public Job add(Job entity) {
        return dbService.persist(entity);
    }

    public void update(Job entity) {
        dbService.update(entity);
    }

    public List<Job> getJobs() {
    	return dbService.findAll(Job.class);
//        Subject subject = SecurityUtils.getSubject();
//        String username = subject.getPrincipal().toString();
//         List<Job> query = dbService.query(new OSQLSynchQuery<Job>("select * from Job"), Job.class);
         //return null;//query.stream().filter(c -> {return c.getOwner().getUsername().equals(username);}).collect(Collectors.toList());
    }

    public List<String> getJobsAsJson() {
        return dbService.findAllAsJsonList(Job.class, SkysailUser.class);
    }

    public void delete(String id) {
        dbService.delete(id);
    }

}
