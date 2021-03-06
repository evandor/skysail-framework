package io.skysail.server.db.it.one2many.todo;

import java.util.Collections;

import org.osgi.service.component.annotations.*;

import io.skysail.domain.core.repos.DbRepository;
import io.skysail.server.db.*;
import io.skysail.server.db.it.one2many.comment.Comment;
import lombok.extern.slf4j.Slf4j;

@Component(immediate = true, property = "name=TodoRepository")
@Slf4j
public class TodoRepository extends GraphDbRepository<Todo> implements DbRepository {

    @Activate
    public void activate() {
        super.activate(Todo.class,Comment.class);
        dbService.executeUpdate("DELETE vertex Todo", Collections.emptyMap());
        dbService.executeUpdate("DELETE vertex Comment", Collections.emptyMap());
    }

    @Reference
    public void setDbService(DbService dbService) {
        log.debug("setting dbService");
        this.dbService = dbService;
    }

    public void unsetDbService(DbService dbService) {
        log.debug("unsetting dbService");
        this.dbService = null;
    }


}