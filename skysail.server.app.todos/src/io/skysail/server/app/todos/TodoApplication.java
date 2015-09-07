package io.skysail.server.app.todos;

import io.skysail.server.app.*;
import io.skysail.server.app.todos.charts.ListChartResource;
import io.skysail.server.app.todos.columns.ListAsColumnsResource;
import io.skysail.server.app.todos.lists.*;
import io.skysail.server.app.todos.repo.TodosRepository;
import io.skysail.server.app.todos.statuses.*;
import io.skysail.server.app.todos.todos.Todo;
import io.skysail.server.app.todos.todos.resources.*;
import io.skysail.server.db.DbRepository;
import io.skysail.server.queryfilter.Filter;
import io.skysail.server.restlet.resources.SkysailServerResource;

import java.util.*;

import lombok.extern.slf4j.Slf4j;

import org.apache.shiro.SecurityUtils;
import org.restlet.Request;

import aQute.bnd.annotation.component.*;
import de.twenty11.skysail.server.app.ApplicationProvider;
import de.twenty11.skysail.server.core.restlet.*;
import de.twenty11.skysail.server.services.*;

@Component(immediate = true)
@Slf4j
public class TodoApplication extends SkysailApplication implements ApplicationProvider, MenuItemProvider {

    public static final String LIST_ID = "id";
    public static final String TODO_ID = "id";
    public static final String APP_NAME = "Todos";

    private TodosRepository todosRepo;

    public TodoApplication() {
        super(APP_NAME, new ApiVersion(2));
        documentEntities(new Todo(), new TodoList());
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
        router.attach(new RouteBuilder("/Lists/parent:null/Todos/", PostTodoWoListResource.class));
        router.attach(new RouteBuilder("/Lists/{"+LIST_ID+"}/Todos/", PostTodoResource.class));
        router.attach(new RouteBuilder("/Lists/{"+LIST_ID+"}/Todos", TodosResource.class));
        router.attach(new RouteBuilder("/Lists/{"+LIST_ID+"}/ArchivedTodos", ArchivedTodosResource.class));

        router.attach(new RouteBuilder("/Todos/_columns", ListAsColumnsResource.class));

        router.attach(new RouteBuilder("/Todos/{"+TODO_ID+"}", TodoResource.class));
        router.attach(new RouteBuilder("/Todos/{"+TODO_ID+"}/", PutTodoResource.class));


    }

    public List<MenuItem> getMenuEntries() {
        MenuItem appMenu = new MenuItem(APP_NAME, "/" + APP_NAME + getApiVersion().getVersionPath(), this);
        appMenu.setCategory(MenuItem.Category.APPLICATION_MAIN_MENU);
        return Arrays.asList(appMenu);
    }

    public List<TodoList> getUsersDefaultLists(Request request) {
        Filter filter = new Filter(request);
        filter.add("owner", SecurityUtils.getSubject().getPrincipal().toString());
        filter.add("defaultList", "true");

        return getRepository().findAllLists(filter, null);
    }

    public void removeDefaultFlag(List<TodoList> usersDefaultLists) {
        for (TodoList todoList : usersDefaultLists) {
            todoList.setDefaultList(false);
            log.info("removing default-List Flag from todo list with id '{}'", todoList.getId());
            getRepository().update(todoList.getId(), todoList);
        }
    }

    public String getDefaultList(Request request) {
        List<TodoList> usersDefaultLists = getUsersDefaultLists(request);
        if (usersDefaultLists.size() > 0) {
            return usersDefaultLists.get(0).getId();
        }
        return null;
    }

    public int getListCount(Request request) {
        Filter filter = new Filter(request);
        filter.add("owner", SecurityUtils.getSubject().getPrincipal().toString());

        return getRepository().findAllLists(filter).size();
    }

    public int getTodosCount(Request request) {
        String owner = SecurityUtils.getSubject().getPrincipal().toString();
        return getRepository().findAllTodos(new Filter(request).add("owner", owner)).size();
    }

    public List<Class<? extends SkysailServerResource<?>>> getMainLinks() {
        List<Class<? extends SkysailServerResource<?>>> result = new ArrayList<>();
        result.add(Top10TodosResource.class);
        result.add(ListsResource.class);
        result.add(ListAsColumnsResource.class);
        return result;
    }

}