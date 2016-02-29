package io.skysail.server.app.whatAbout;

import java.util.*;

import org.osgi.service.component.annotations.*;

import io.skysail.domain.core.Repositories;
import io.skysail.server.ApplicationContextId;
import io.skysail.server.app.ApiVersion;

@Component(immediate = true)
public class WhatAboutApplication extends WhatAboutApplicationGen {

    public  WhatAboutApplication() {
        super("WhatAbout", new ApiVersion(1), Arrays.asList());
        //addToAppContext(ApplicationContextId.IMG, "/static/img/silk/page_link.png");
    }

    @Reference(policy = ReferencePolicy.DYNAMIC, cardinality = ReferenceCardinality.MANDATORY)
    public void setRepositories(Repositories repos) {
        super.setRepositories(repos);
    }

    public void unsetRepositories(Repositories repo) {
        super.setRepositories(null);
    }

}