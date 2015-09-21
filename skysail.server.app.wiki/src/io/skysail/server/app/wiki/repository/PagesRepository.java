package io.skysail.server.app.wiki.repository;

import io.skysail.server.app.wiki.pages.Page;
import io.skysail.server.app.wiki.versions.Version;
import io.skysail.server.db.*;
import io.skysail.server.repo.DbRepository;
import aQute.bnd.annotation.component.*;

@Component(immediate = true, properties = "name=PagesRepository")
public class PagesRepository extends GraphDbRepository<Page> implements DbRepository {

    @Activate
    public void activate() {
        dbService.createWithSuperClass("V", Page.class.getSimpleName());
        dbService.register(Page.class);
        dbService.createWithSuperClass("V", Version.class.getSimpleName());
        dbService.register(Version.class);
    }

    @Reference
    public void setDbService(DbService dbService) {
        this.dbService = dbService;
    }

    public void unsetDbService(DbService dbService) {
        this.dbService = null;
    }

}
