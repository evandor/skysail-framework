package io.skysail.server.app.bookmarks.repo;

import org.osgi.service.component.annotations.*;

import io.skysail.domain.core.repos.DbRepository;
import io.skysail.server.app.bookmarks.Bookmark;
import io.skysail.server.db.*;
import lombok.extern.slf4j.Slf4j;

@Component(immediate = true, property = "name=BookmarksRepository")
@Slf4j
public class BookmarksRepository extends GraphDbRepository<Bookmark> implements DbRepository {

//    @Reference
//    private volatile DbService dbService;
    
    @Reference
    public void setDbService(DbService dbService) {
        log.debug("setting dbService");
        this.dbService = dbService;
    }

    public void unsetDbService(DbService dbService) {
        log.debug("unsetting dbService");
        this.dbService = null;
    }
    @Activate
    public void activate() {
        log.debug("activating VersionsRepository");
        dbService.createWithSuperClass("V", DbClassName.of(Bookmark.class));
        dbService.register(Bookmark.class);
    }

    

}