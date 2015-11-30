package io.skysail.server.app.wiki.repository;

import org.osgi.service.component.annotations.*;

import io.skysail.api.repos.DbRepository;
import io.skysail.server.app.wiki.pages.Page;
import io.skysail.server.app.wiki.versions.Version;
import io.skysail.server.db.*;

@Component(immediate = true, property = "name=PagesRepository")
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
