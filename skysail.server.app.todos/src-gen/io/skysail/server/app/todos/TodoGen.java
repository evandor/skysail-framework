package io.skysail.server.app.todos;

import java.util.Arrays;
import java.util.List;

import aQute.bnd.annotation.component.Component;
import aQute.bnd.annotation.component.Reference;
import de.twenty11.skysail.server.app.ApplicationProvider;
import de.twenty11.skysail.server.app.SkysailApplication;

import io.skysail.server.app.todos.domain.PostTodoResource;

import io.skysail.server.app.todos.domain.PutTodoResource;

import io.skysail.server.app.todos.domain.TodosResource;

import io.skysail.server.app.todos.domain.TodoResource;


import de.twenty11.skysail.server.core.restlet.ApplicationContextId;
import de.twenty11.skysail.server.core.restlet.RouteBuilder;
import de.twenty11.skysail.server.services.MenuItem;
import de.twenty11.skysail.server.services.MenuItemProvider;

@Component(immediate = true)
@javax.annotation.Generated(value = "de.twenty11.skysail.server.ext.apt.GenerateSkysailApplicationProcessor")
public class TodoGen extends SkysailApplication implements MenuItemProvider, ApplicationProvider {

	private static final String APP_NAME = "TodoGen";

	public TodoGen() {
		super(APP_NAME);
		addToAppContext(ApplicationContextId.IMG, "/static/img/silk/page_link.png");
	}

	@Override
	protected void attach() {
	    // Application root resource
		router.attach(new RouteBuilder("", RootResource.class));

		router.attach(new RouteBuilder("/Todos/", PostTodoResource.class));

		router.attach(new RouteBuilder("/Todos", TodosResource.class));

		router.attach(new RouteBuilder("/Todos/{id}", TodoResource.class));

		router.attach(new RouteBuilder("/Todos/{id}/", PutTodoResource.class));

	}

	public List<MenuItem> getMenuEntries() {
		MenuItem appMenu = new MenuItem("TodoGen", "/TodoGen", this);
		appMenu.setCategory(MenuItem.Category.APPLICATION_MAIN_MENU);
		return Arrays.asList(appMenu);
	}

}