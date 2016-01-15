package io.skysail.server.app.wiki;

import java.util.*;

import org.osgi.service.component.annotations.*;

import de.twenty11.skysail.server.core.restlet.ApplicationContextId;
import io.skysail.domain.core.Repositories;
import io.skysail.server.app.ApiVersion;

@Component(immediate = true)
public class WikiApplication extends WikiApplicationGen {

    public  WikiApplication() {
        super("Wiki", new ApiVersion(1), Arrays.asList());
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