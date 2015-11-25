package io.skysail.server.designer.csvreader;

import io.skysail.api.repos.*;
import io.skysail.server.app.SkysailApplication;
import io.skysail.server.menus.*;

import java.util.*;

import aQute.bnd.annotation.component.*;
import de.twenty11.skysail.server.app.ApplicationProvider;
import de.twenty11.skysail.server.core.restlet.*;

@Component(immediate = true)
public class CsvReaderApplication extends SkysailApplication implements ApplicationProvider, MenuItemProvider {

    public static final String LIST_ID = "lid";
    public static final String TODO_ID = "id";
    public static final String APP_NAME = "CsvReader";

    private CsvReaderRepository repo;

    public CsvReaderApplication() {
        super(APP_NAME);
        addToAppContext(ApplicationContextId.IMG, "/static/img/silk/page_link.png");
    }

    @Reference(dynamic = true, multiple = false, optional = false, target = "(name=CsvReaderRepository)")
    public void setRepository(DbRepository repo) {
        this.repo = (CsvReaderRepository) repo;
    }

    public void unsetRepository(DbRepository repo) {
        this.repo = null;
    }

    public Repository getRepository() {
        return (Repository) repo;
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