package io.skysail.server.app.contacts.domain;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import aQute.bnd.annotation.component.Activate;
import aQute.bnd.annotation.component.Component;
import aQute.bnd.annotation.component.Deactivate;
import aQute.bnd.annotation.component.Reference;

import com.orientechnologies.orient.core.record.impl.ODocument;
import com.orientechnologies.orient.core.sql.query.OSQLSynchQuery;

import de.twenty11.skysail.server.core.db.DbService;

@Component
public class ContactsRepository {

    private static ContactsRepository instance;

    private DbService dbService;

    public static ContactsRepository getInstance() {
        // for tests
        if (instance == null) {
            instance = new ContactsRepository();
        }
        return instance;
    }

    @Activate
    public void activate() {
        ContactsRepository.instance = this;
    }

    @Deactivate
    public void deactivate() {
        ContactsRepository.instance = null;
    }

    @Reference
    public void setDbService(DbService dbService) {
        this.dbService = dbService;
    }

    public void unsetDbService(@SuppressWarnings("unused") DbService dbService) {
        this.dbService = null;
    }

    public Contact add(Contact entity) {
        return dbService.persist(entity);
    }

    public List<Contact> getContacts() {
        return dbService.findAll(Contact.class);
    }

    public Contact getById(String id) {
        return dbService.find(id, Contact.class);
    }

    public void update(Contact entity) {
        dbService.update(entity);
    }

    public void delete(String id) {
        dbService.delete(id);
    }

    public List<String> getContactsAsJson(String username) {
        String sql = "SELECT from Contact";// WHERE owner= :username";
        Map<String, Object> params = new HashMap<String, Object>();
        params.put("username", username);
        return dbService.findAndReturnJson(sql, Contact.class, params);

    }

    public long getContactsCount(String username) {
        Map<String, Object> params = new HashMap<String, Object>();
        params.put("username", username);
        return dbService
                .query(new OSQLSynchQuery<ODocument>("select COUNT(*) as count from Contact WHERE owner='" + username
                        + "'"), Contact.class).get(0).field("count");
    }

    public List<String> getContacts(int page, String username, int linesPerPage, int pageSize) {
        String sql = "select * from Contact WHERE owner= :username ORDER BY lastname ASC, firstname ASC SKIP "
                + (linesPerPage * (page - 1)) + " LIMIT " + pageSize;
        Map<String, Object> params = new HashMap<String, Object>();
        params.put("username", username);
        return dbService.findAndReturnJson(sql, Contact.class, params);
        // return dbService.query(new OSQLSynchQuery<Contact>(), Contact.class);
    }

}