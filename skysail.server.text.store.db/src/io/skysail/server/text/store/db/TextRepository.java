package io.skysail.server.text.store.db;

import io.skysail.server.db.DbService;
import io.skysail.server.text.store.db.text.Text;

import java.util.List;

import aQute.bnd.annotation.component.*;

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
        return null;//dbService.persist(entity);
    }

    public List<Text> getContacts() {
        return null;//dbService.findAll(Text.class);
    }

    public Text getById(String id) {
        return null;//dbService.find(id);
    }

    public void update(Text entity) {
        //dbService.update(entity);
    }

    public void delete(String id) {
       //.delete(id);
    }

}