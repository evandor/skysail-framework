package io.skysail.server.ext.apt.test.withlist.folders;

import java.util.List;


import aQute.bnd.annotation.component.Activate;
import aQute.bnd.annotation.component.Component;
import aQute.bnd.annotation.component.Deactivate;
import aQute.bnd.annotation.component.Reference;
import de.twenty11.skysail.server.core.db.DbService;

@Component
public class FoldersRepository {

	private static FoldersRepository instance;

	private DbService dbService;

	public static FoldersRepository getInstance() {
		// for tests
		if (instance == null) {
			instance = new FoldersRepository();
		}
	    return instance;
    }

	@Activate
	public void activate() {
		FoldersRepository.instance = this;
	}

	@Deactivate
	public void deactivate() {
		FoldersRepository.instance = null;
	}

	@Reference
	public void setDbService(DbService dbService) {
		this.dbService = dbService;
	}

	public void unsetDbService(@SuppressWarnings("unused") DbService dbService) {
		this.dbService = null;
	}

	public Folder add(Folder entity) {
		return dbService.persist(entity);
    }

	public List<Folder> getFolders() {
	    return dbService.findAll(Folder.class);
    }

	public Folder getById(String id) {
	    return dbService.find(id);
    }

	public void update(Folder entity) {
		dbService.update(entity);
    }

    public void delete(String id) {
        dbService.delete(id);
    }

}