package io.skysail.server.designer.demo.accounts;

import java.util.*;

import org.osgi.service.component.annotations.Component;
import org.osgi.service.component.annotations.Reference;
import org.osgi.service.component.annotations.ReferenceCardinality;
import org.osgi.service.component.annotations.ReferencePolicy;

import io.skysail.server.app.ApplicationProvider;
import io.skysail.domain.core.Repositories;
import io.skysail.server.app.ApiVersion;
import io.skysail.server.menus.MenuItemProvider;

@Component(immediate = true)
public class AccountsApplication extends AccountsApplicationGen implements ApplicationProvider, MenuItemProvider {

    public  AccountsApplication() {
        super("Accounts", new ApiVersion(1), Arrays.asList());
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