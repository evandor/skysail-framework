package io.skysail.server.app.bookmarks.repo;

import io.skysail.api.repos.DbRepository;
import io.skysail.server.app.bookmarks.Bookmark;
import io.skysail.server.db.*;
import lombok.extern.slf4j.Slf4j;
import aQute.bnd.annotation.component.*;

@Component(immediate = true, properties = "name=BookmarksRepository")
@Slf4j
public class BookmarksRepository extends GraphDbRepository<Bookmark> implements DbRepository {

    @Activate
    public void activate() {
        log.debug("activating VersionsRepository");
        dbService.createWithSuperClass("V", DbClassName.of(Bookmark.class));
        dbService.register(Bookmark.class);
    }

    @Reference
    public void setDbService(DbService dbService) {
        this.dbService = dbService;
    }

    public void unsetDbService(DbService dbService) {
        this.dbService = null;
    }

}