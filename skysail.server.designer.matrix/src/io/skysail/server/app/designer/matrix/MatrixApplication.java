package io.skysail.server.app.designer.matrix;

import io.skysail.api.repos.DbRepository;
import io.skysail.server.app.SkysailApplication;
import io.skysail.server.menus.*;

import java.util.*;

import aQute.bnd.annotation.component.*;
import de.twenty11.skysail.server.app.ApplicationProvider;
import de.twenty11.skysail.server.core.restlet.ApplicationContextId;

@Component(immediate = true)
public class MatrixApplication extends SkysailApplication implements ApplicationProvider, MenuItemProvider {

    public static final String LIST_ID = "lid";
    public static final String TODO_ID = "id";
    public static final String APP_NAME = "Matrix";

    private ContactRepo repo;

    public MatrixApplication() {
        super(APP_NAME);
        addToAppContext(ApplicationContextId.IMG, "/static/img/silk/page_link.png");
    }

    @Reference(dynamic = true, multiple = false, optional = false, target = "(name=MatrixRepository)")
    public void setRepository(DbRepository repo) {
        this.repo = (ContactRepo) repo;
    }

    public void unsetRepository(DbRepository repo) {
        this.repo = null;
    }

    @Override
    protected void attach() {
        super.attach();

    }

    public List<MenuItem> getMenuEntries() {
        MenuItem appMenu = new MenuItem(APP_NAME, "/" + APP_NAME, this);
        appMenu.setCategory(MenuItem.Category.APPLICATION_MAIN_MENU);
        return Arrays.asList(appMenu);
    }


}