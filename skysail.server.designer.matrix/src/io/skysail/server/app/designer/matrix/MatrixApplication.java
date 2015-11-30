package io.skysail.server.app.designer.matrix;

import java.util.*;

import org.osgi.service.component.annotations.*;
import org.osgi.service.event.EventAdmin;

import de.twenty11.skysail.server.app.ApplicationProvider;
import de.twenty11.skysail.server.core.restlet.ApplicationContextId;
import io.skysail.api.repos.*;
import io.skysail.server.app.SkysailApplication;
import io.skysail.server.menus.*;
import lombok.Getter;

@Component(immediate = true)
public class MatrixApplication extends SkysailApplication implements ApplicationProvider, MenuItemProvider {

    public static final String LIST_ID = "lid";
    public static final String TODO_ID = "id";
    public static final String APP_NAME = "Matrix";

    private MatrixRepository repo;

    @Getter
    private EventAdmin eventAdmin;
    
    public MatrixApplication() {
        super(APP_NAME);
        addToAppContext(ApplicationContextId.IMG, "/static/img/silk/page_link.png");
    }

    @org.osgi.service.component.annotations.Reference(policy = ReferencePolicy.DYNAMIC, cardinality = ReferenceCardinality.MANDATORY, target = "(name=MatrixRepository)")
    public void setRepository(DbRepository repo) {
        this.repo = (MatrixRepository) repo;
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