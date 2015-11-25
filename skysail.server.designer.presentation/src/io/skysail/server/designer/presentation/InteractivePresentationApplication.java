package io.skysail.server.designer.presentation;

import io.skysail.api.repos.*;
import io.skysail.server.app.*;
import io.skysail.server.menus.MenuItemProvider;

import java.util.Arrays;

import aQute.bnd.annotation.component.*;
import de.twenty11.skysail.server.app.ApplicationProvider;
import de.twenty11.skysail.server.core.restlet.ApplicationContextId;

@Component(immediate = true)
public class InteractivePresentationApplication extends SkysailApplication implements ApplicationProvider, MenuItemProvider {

    public static final String LIST_ID = "lid";
    public static final String TODO_ID = "id";
    public static final String APP_NAME = "InteractivePresentation";

    private TopicRepo repo;

    public InteractivePresentationApplication() {
        super(APP_NAME, new ApiVersion(1), Arrays.asList(Topic.class));
        addToAppContext(ApplicationContextId.IMG, "/static/img/silk/page_link.png");
    }

    @Reference(dynamic = true, multiple = false, optional = false, target = "(name=TopicRepository)")
    public void setRepository(DbRepository repo) {
        this.repo = (TopicRepo) repo;
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

}