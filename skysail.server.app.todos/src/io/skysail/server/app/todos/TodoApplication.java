package io.skysail.server.app.todos;

import io.skysail.server.app.SkysailApplication;
import io.skysail.server.app.todos.charts.ListChartResource;
import io.skysail.server.app.todos.lists.*;
import io.skysail.server.app.todos.repo.TodosRepository;
import io.skysail.server.app.todos.statuses.*;
import io.skysail.server.app.todos.todos.resources.*;
import io.skysail.server.db.DbRepository;

import java.util.*;

import aQute.bnd.annotation.component.*;
import de.twenty11.skysail.server.app.ApplicationProvider;
import de.twenty11.skysail.server.core.restlet.*;
import de.twenty11.skysail.server.services.*;

@Component(immediate = true)
public class TodoApplication extends SkysailApplication implements ApplicationProvider, MenuItemProvider {

    public static final String LIST_ID = "lid";
    public static final String TODO_ID = "id";
    public static final String APP_NAME = "Todos";

    private TodosRepository todosRepo;

    public TodoApplication() {
        super(APP_NAME);
        addToAppContext(ApplicationContextId.IMG, "/static/img/silk/tag_yellow.png");
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
        router.attach(new RouteBuilder("", Top10TodosResource.class));

        router.attach(new RouteBuilder("/Statuses", StatusesResource.class));
        router.attach(new RouteBuilder("/Statuses/{statusname}", NextStatusesResource.class));

        router.attach(new RouteBuilder("/Lists", ListsResource.class));
        router.attach(new RouteBuilder("/Lists/", PostListResource.class));
        router.attach(new RouteBuilder("/Lists/{"+LIST_ID+"}", ListResource.class));
        router.attach(new RouteBuilder("/Lists/{"+LIST_ID+"}/", PutListResource.class));

        router.attach(new RouteBuilder("/Lists/{"+LIST_ID+"}/_stats", ListChartResource.class));

        router.attach(new RouteBuilder("/Lists/list:null/Todos/", PostTodoWoListResource.class));

        router.attach(new RouteBuilder("/Lists/{"+LIST_ID+"}/Todos/", PostTodoResource.class));
        router.attach(new RouteBuilder("/Lists/{"+LIST_ID+"}/Todos", TodosResource.class));
        router.attach(new RouteBuilder("/Lists/{"+LIST_ID+"}/Todos/{"+TODO_ID+"}", TodoResource.class));
        router.attach(new RouteBuilder("/Lists/{"+LIST_ID+"}/Todos/{"+TODO_ID+"}/", PutTodoResource.class));

        router.attach(new RouteBuilder("/Lists/{"+LIST_ID+"}/ArchivedTodos", ArchivedTodosResource.class));

    }

    public List<MenuItem> getMenuEntries() {
        MenuItem appMenu = new MenuItem(APP_NAME, "/" + APP_NAME, this);
        appMenu.setCategory(MenuItem.Category.APPLICATION_MAIN_MENU);
        return Arrays.asList(appMenu);
    }

}