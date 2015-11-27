package io.skysail.server.designer.checklist;

import aQute.bnd.annotation.component.*;
import io.skysail.api.domain.Identifiable;
import io.skysail.api.repos.DbRepository;
import io.skysail.server.db.DbService;

public class ChecklistRepository implements DbRepository {

    private DbService dbService;

    @Activate
    public void activate() {
        //log.debug("activating VersionsRepository");
        //dbService.createWithSuperClass("V", DbClassName.of(Bookmark.class));
        //dbService.register(Bookmark.class);
    }

    @Reference
    public void setDbService(DbService dbService) {
        this.dbService = dbService;
    }

    public void unsetDbService(DbService dbService) {
        this.dbService = null;
    }

    @Override
    public Class<? extends Identifiable> getRootEntity() {
        return null;
    }

    @Override
    public Identifiable findOne(String id) {
        return null;
    }

    @Override
    public Object save(Identifiable identifiable) {
        return null;
    }

    @Override
    public Object update(String id, Identifiable entity, String... edges) {
        return null;
    }
}