package io.skysail.server.ext.apt.test.crm.contacts;

import java.util.List;


import aQute.bnd.annotation.component.Activate;
import aQute.bnd.annotation.component.Component;
import aQute.bnd.annotation.component.Deactivate;
import aQute.bnd.annotation.component.Reference;
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
	    return dbService.find(id);
    }

	public void update(Contact entity) {
		dbService.update(entity);
    }

    public void delete(String id) {
        dbService.delete(id);
    }

}