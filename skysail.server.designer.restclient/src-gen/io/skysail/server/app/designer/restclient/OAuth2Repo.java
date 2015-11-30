package io.skysail.server.app.designer.restclient;

import javax.annotation.Generated;

import org.osgi.service.component.annotations.*;

import io.skysail.api.repos.DbRepository;
import io.skysail.server.db.*;
import lombok.extern.slf4j.Slf4j;

@Component(immediate = true, property = "name=OAuth2Repository")
@Slf4j
@Generated("io.skysail.server.codegen.apt.processors.EntityProcessor")
public class OAuth2Repo extends GraphDbRepository<OAuth2> implements DbRepository {

    @Activate
    public void activate() {
        super.activate(OAuth2.class);
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



