package io.skysail.server.app.facebookClient;


import org.osgi.service.component.annotations.*;

import io.skysail.domain.core.repos.DbRepository;
import io.skysail.server.db.*;

@Component(immediate = true, property = "name=OAuthConfigsRepository")
public class OAuthConfigRepository extends GraphDbRepository<io.skysail.server.app.facebookClient.OAuthConfig> implements DbRepository {

    @Reference
    public void setDbService(DbService dbService) {
        this.dbService = dbService;
    }

    public void unsetDbService(DbService dbService) {
        this.dbService = null;
    }

    @Activate
    public void activate() {
        //log.debug("activating io.skysail.server.app.facebookClient.OAuthConfig" Repository);
        dbService.createWithSuperClass("V", DbClassName.of(io.skysail.server.app.facebookClient.OAuthConfig.class));
        dbService.register(OAuthConfig.class);
    }

}