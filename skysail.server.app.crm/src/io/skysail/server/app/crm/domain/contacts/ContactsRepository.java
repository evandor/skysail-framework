//package io.skysail.server.app.crm.domain.contacts;
//
//import java.util.List;
//
//import org.codehaus.jettison.json.JSONObject;
//
//import aQute.bnd.annotation.component.Activate;
//import aQute.bnd.annotation.component.Component;
//import aQute.bnd.annotation.component.Deactivate;
//import aQute.bnd.annotation.component.Reference;
//import de.twenty11.skysail.server.core.db.DbService;
//import de.twenty11.skysail.server.core.db.DbService2;
//
//@Component
//public class ContactsRepository {
//
//    private static ContactsRepository instance;
//
//    private DbService2 dbService;
//
//    public static ContactsRepository getInstance() {
//        // for tests
//        if (instance == null) {
//            instance = new ContactsRepository();
//        }
//        return instance;
//    }
//
//    @Activate
//    public void activate() {
//        ContactsRepository.instance = this;
//    }
//
//    @Deactivate
//    public void deactivate() {
//        ContactsRepository.instance = null;
//    }
//
//    @Reference
//    public void setDbService(DbService2 dbService) {
//        this.dbService = dbService;
//    }
//
//    public void unsetDbService(DbService dbService) {
//        this.dbService = null;
//    }
//
//    // public Contact add(Contact entity) {
//    // return dbService.persist(entity);
//    // }
//    //
//    // public List<Contact> getContacts() {
//    // return dbService.findAll(Contact.class);
//    // }
//    //
//    public Contact getById(String id) {
//        return dbService.findObjectById(Contact.class, id);
//    }
//
//    //
//    // public void update(Contact entity) {
//    // dbService.update(entity);
//    // }
//    //
//    // public void delete(String id) {
//    // dbService.delete(id);
//    // }
//
//    // public List<String> getContactsAsJson(String username) {
//    // String sql = "SELECT from Contact";// WHERE owner= :username";
//    // Map<String, Object> params = new HashMap<String, Object>();
//    // params.put("username", username);
//    // return dbService.findAndReturnJson(sql, Contact.class, params);
//    //
//    // }
//    //
//    // public long getContactsCount(String username) {
//    // Map<String, Object> params = new HashMap<String, Object>();
//    // params.put("username", username);
//    // return dbService
//    // .query(new
//    // OSQLSynchQuery<ODocument>("select COUNT(*) as count from Contact WHERE owner='"
//    // + username
//    // + "'"), Contact.class).get(0).field("count");
//    // }
//
//    public List<String> getContacts(int page, String username, int linesPerPage, int pageSize) {
//        // String sql =
//        // "select * from Contact WHERE owner= :username ORDER BY lastname ASC, firstname ASC SKIP "
//        // + (linesPerPage * (page - 1)) + " LIMIT " + pageSize;
//        // Map<String, Object> params = new HashMap<String, Object>();
//        // params.put("username", username);
//        // return dbService.findAndReturnJson(sql, Contact.class, params);
//        return dbService.getAll(Contact.class, username);
//    }
//
//    public List<Contact> getContacts(String username) {
//        return dbService.findObjects(Contact.class, username);
//    }
//
//    public Contact add(Contact entity) {
//        Object id = dbService.persist(entity, "worksFor");
//        return null;
//    }
//
//    public void update(JSONObject json) {
//        dbService.update(json);
//    }
// }