//package io.skysail.server.app.quartz.jobs;
//
//import io.skysail.server.db.DbService;
//
//import java.util.List;
//
//import aQute.bnd.annotation.component.*;
//
//@Component
//public class JobsRepository {
//
//    private DbService dbService;
//
//	private static JobsRepository instance;
//
//    @Activate
//    public void activate() {
//    	JobsRepository.instance = this;
//    }
//
//    @Deactivate
//    public void deactivate() {
//    	JobsRepository.instance = null;
//    }
//
//    @Reference //(dynamic = true, multiple = false, optional = false)
//    public void setDbService(DbService dbservice) {
//    	this.dbService = dbservice;
//    }
//
//    public static JobsRepository getInstance() {
//    	return JobsRepository.instance;
//    }
//
//    public void unsetDbService(@SuppressWarnings("unused") DbService dbservice) {
//    	this.dbService = null;
//    }
//
//    public Job getById(String id) {
//        return null;// dbService.find(id, Job.class);
//    }
//
//    public String getByIdAsJson(String id) {
//        return null;// dbService.findAndReturnJson(id, Job.class);
//    }
//
//    public Job add(Job entity) {
//        return null;// dbService.persist(entity);
//    }
//
//    public void update(Job entity) {
//        //dbService.update(entity);
//    }
//
//    public List<Job> getJobs() {
//    	return null;// dbService.findAll(Job.class);
////        Subject subject = SecurityUtils.getSubject();
////        String username = subject.getPrincipal().toString();
////         List<Job> query = dbService.query(new OSQLSynchQuery<Job>("select * from Job"), Job.class);
//         //return null;//query.stream().filter(c -> {return c.getOwner().getUsername().equals(username);}).collect(Collectors.toList());
//    }
//
//    public List<String> getJobsAsJson() {
//        return null;//  dbService.findAllAsJsonList(Job.class, SkysailUser.class);
//    }
//
//    public void delete(String id) {
//        //dbService.delete(id);
//    }
//
//}
