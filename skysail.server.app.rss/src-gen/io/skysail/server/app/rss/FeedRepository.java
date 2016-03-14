package io.skysail.server.app.rss;


import org.osgi.service.component.annotations.*;

import io.skysail.domain.core.repos.DbRepository;
import io.skysail.server.db.*;

/**
 * generated from repository.stg
 */
@Component(immediate = true, property = "name=FeedsRepository")
public class FeedRepository extends GraphDbRepository<io.skysail.server.app.rss.feed.Feed> implements DbRepository {

    @Reference
    public void setDbService(DbService dbService) {
        this.dbService = dbService;
    }

    public void unsetDbService(DbService dbService) {
        this.dbService = null;
    }

    @Activate
    public void activate() {
        //log.debug("activating io.skysail.server.app.rss.feed.Feed" Repository);
        dbService.createWithSuperClass("V", DbClassName.of(io.skysail.server.app.rss.feed.Feed.class));
        dbService.register(io.skysail.server.app.rss.feed.Feed.class);
    }

}