package io.skysail.server.app.notes;


import org.osgi.service.component.annotations.*;

import io.skysail.domain.core.repos.DbRepository;
import io.skysail.server.db.*;

@Component(immediate = true, property = "name=NotesRepository")
public class NoteRepository extends GraphDbRepository<io.skysail.server.app.notes.Note> implements DbRepository {

    @Reference
    public void setDbService(DbService dbService) {
        this.dbService = dbService;
    }

    public void unsetDbService(DbService dbService) {
        this.dbService = null;
    }

    @Activate
    public void activate() {
        //log.debug("activating io.skysail.server.app.notes.Note" Repository);
        dbService.createWithSuperClass("V", DbClassName.of(io.skysail.server.app.notes.Note.class));
        dbService.register(Note.class);
    }

}