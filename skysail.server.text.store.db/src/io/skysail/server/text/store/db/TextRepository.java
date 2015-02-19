package io.skysail.server.text.store.db;

import io.skysail.server.text.store.db.text.Text;

import java.util.List;

import aQute.bnd.annotation.component.Activate;
import aQute.bnd.annotation.component.Component;
import aQute.bnd.annotation.component.Deactivate;
import aQute.bnd.annotation.component.Reference;
import de.twenty11.skysail.server.core.db.DbService;

@Component
public class TextRepository {

    private static TextRepository instance;

    private DbService dbService;

    public static TextRepository getInstance() {
        // for tests
        if (instance == null) {
            instance = new TextRepository();
        }
        return instance;
    }

    @Activate
    public void activate() {
        TextRepository.instance = this;
    }

    @Deactivate
    public void deactivate() {
        TextRepository.instance = null;
    }

    @Reference
    public void setDbService(DbService dbService) {
        this.dbService = dbService;
    }

    public void unsetDbService(DbService dbService) {
        this.dbService = null;
    }

    public Text add(Text entity) {
        return dbService.persist(entity);
    }

    public List<Text> getContacts() {
        return dbService.findAll(Text.class);
    }

    public Text getById(String id) {
        return dbService.find(id);
    }

    public void update(Text entity) {
        dbService.update(entity);
    }

    public void delete(String id) {
        dbService.delete(id);
    }

}