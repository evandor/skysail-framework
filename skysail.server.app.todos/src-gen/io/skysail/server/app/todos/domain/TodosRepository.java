package io.skysail.server.app.todos.domain;

import java.util.List;


import aQute.bnd.annotation.component.Activate;
import aQute.bnd.annotation.component.Component;
import aQute.bnd.annotation.component.Deactivate;
import aQute.bnd.annotation.component.Reference;
import de.twenty11.skysail.server.core.db.DbService;

@Component
public class TodosRepository {

	private static TodosRepository instance;

	private DbService dbService;

	public static TodosRepository getInstance() {
		// for tests
		if (instance == null) {
			instance = new TodosRepository();
		}
	    return instance;
    }

	@Activate
	public void activate() {
		TodosRepository.instance = this;
	}

	@Deactivate
	public void deactivate() {
		TodosRepository.instance = null;
	}

	@Reference
	public void setDbService(DbService dbService) {
		this.dbService = dbService;
	}

	public void unsetDbService(@SuppressWarnings("unused") DbService dbService) {
		this.dbService = null;
	}

	public Todo add(Todo entity) {
		return dbService.persist(entity);
    }

	public List<Todo> getTodos() {
	    return dbService.findAll(Todo.class);
    }

	public Todo getById(String id) {
	    return dbService.find(id);
    }

	public void update(Todo entity) {
		dbService.update(entity);
    }

    public void delete(String id) {
        dbService.delete(id);
    }

}