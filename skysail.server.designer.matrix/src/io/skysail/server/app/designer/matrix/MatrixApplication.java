package io.skysail.server.app.designer.matrix;

import io.skysail.api.repos.DbRepository;
import io.skysail.server.app.*;
import io.skysail.server.menus.MenuItemProvider;

import java.util.Arrays;

import aQute.bnd.annotation.component.*;
import de.twenty11.skysail.server.app.ApplicationProvider;
import de.twenty11.skysail.server.core.restlet.ApplicationContextId;

@Component(immediate = true)
public class MatrixApplication extends SkysailApplication implements ApplicationProvider, MenuItemProvider {

    public static final String LIST_ID = "lid";
    public static final String TODO_ID = "id";
    public static final String APP_NAME = "Matrix";

    public MatrixApplication() {
        super(APP_NAME, new ApiVersion(1), Arrays.asList(Contact.class));
        addToAppContext(ApplicationContextId.IMG, "/static/img/silk/page_link.png");
    }

    @Reference(dynamic = true, multiple = false, optional = false)
    public void setRepositories(Repositories repos) {
        super.setRepositories(repos);
    }

    public void unsetRepositories(DbRepository repo) {
        super.setRepositories(null);
    }

}