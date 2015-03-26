package io.skysail.server.app.todos;

import io.skysail.server.app.todos.domain.resources.PostTodoResource;
import io.skysail.server.app.todos.domain.resources.PutTodoResource;
import io.skysail.server.app.todos.domain.resources.TodoResource;
import io.skysail.server.app.todos.domain.resources.TodosRepository;
import io.skysail.server.app.todos.domain.resources.TodosResource;
import io.skysail.server.app.todos.lists.ListsResource;
import io.skysail.server.app.todos.lists.PostListResource;

import java.util.Arrays;
import java.util.List;

import aQute.bnd.annotation.component.Component;
import aQute.bnd.annotation.component.Reference;
import de.twenty11.skysail.server.app.ApplicationProvider;
import de.twenty11.skysail.server.app.SkysailApplication;
import de.twenty11.skysail.server.core.db.DbRepository;
import de.twenty11.skysail.server.core.restlet.ApplicationContextId;
import de.twenty11.skysail.server.core.restlet.RouteBuilder;
import de.twenty11.skysail.server.services.MenuItem;
import de.twenty11.skysail.server.services.MenuItemProvider;

@Component(immediate = true)
public class TodoApplication extends SkysailApplication implements ApplicationProvider, MenuItemProvider {

    private static final String APP_NAME = "Todos";
    private TodosRepository todosRepo;

    public TodoApplication() {
        super(APP_NAME);
        addToAppContext(ApplicationContextId.IMG, "/static/img/silk/page_link.png");
    }

    @Reference(dynamic = true, multiple = false, optional = false, target = "(name=TodosRepository)")
    public void setRepository(DbRepository repo) {
        this.todosRepo = (TodosRepository) repo;
    }

    public void unsetRepository(DbRepository repo) {
        this.todosRepo = null;
    }

    public TodosRepository getRepository() {
        return todosRepo;
    }

    @Override
    protected void attach() {
        super.attach();
        // Application root resource
        router.attach(new RouteBuilder("", TodosResource.class));
        router.attach(new RouteBuilder("/Todos/", PostTodoResource.class));
        router.attach(new RouteBuilder("/Todos", TodosResource.class));
        router.attach(new RouteBuilder("/Todos/{id}", TodoResource.class));
        router.attach(new RouteBuilder("/Todos/{id}/", PutTodoResource.class));
        router.attach(new RouteBuilder("/List", ListsResource.class));
        router.attach(new RouteBuilder("/List/", PostListResource.class));
    }

    public List<MenuItem> getMenuEntries() {
        MenuItem appMenu = new MenuItem(APP_NAME, "/" + APP_NAME, this);
        appMenu.setCategory(MenuItem.Category.APPLICATION_MAIN_MENU);
        return Arrays.asList(appMenu);
    }

}