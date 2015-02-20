package io.skysail.server.ext.apt.test.twoentities;

import java.util.Arrays;
import java.util.List;

import aQute.bnd.annotation.component.Component;
import aQute.bnd.annotation.component.Reference;
import de.twenty11.skysail.server.app.ApplicationProvider;
import de.twenty11.skysail.server.app.SkysailApplication;

import io.skysail.server.ext.apt.test.withlist.folders.PostFolderResource;
import io.skysail.server.ext.apt.test.twoentities.schedules.PostScheduleResource;
import io.skysail.server.ext.apt.test.withlist.companies.PostCompanyResource;
import io.skysail.server.ext.apt.test.crm.contacts.PostContactResource;
import io.skysail.server.ext.apt.test.twoentities.jobs.PostJobResource;
import io.skysail.server.ext.apt.test.simple.todos.PostTodoResource;

import io.skysail.server.ext.apt.test.simple.todos.PutTodoResource;
import io.skysail.server.ext.apt.test.withlist.folders.PutFolderResource;
import io.skysail.server.ext.apt.test.withlist.companies.PutCompanyResource;
import io.skysail.server.ext.apt.test.twoentities.schedules.PutScheduleResource;
import io.skysail.server.ext.apt.test.crm.contacts.PutContactResource;
import io.skysail.server.ext.apt.test.twoentities.jobs.PutJobResource;

import io.skysail.server.ext.apt.test.withlist.companies.CompanysResource;
import io.skysail.server.ext.apt.test.withlist.folders.FoldersResource;
import io.skysail.server.ext.apt.test.twoentities.schedules.SchedulesResource;
import io.skysail.server.ext.apt.test.simple.todos.TodosResource;
import io.skysail.server.ext.apt.test.crm.contacts.ContactsResource;
import io.skysail.server.ext.apt.test.twoentities.jobs.JobsResource;

import io.skysail.server.ext.apt.test.twoentities.schedules.ScheduleResource;
import io.skysail.server.ext.apt.test.withlist.companies.CompanyResource;
import io.skysail.server.ext.apt.test.crm.contacts.ContactResource;
import io.skysail.server.ext.apt.test.simple.todos.TodoResource;
import io.skysail.server.ext.apt.test.withlist.folders.FolderResource;
import io.skysail.server.ext.apt.test.twoentities.jobs.JobResource;


import de.twenty11.skysail.server.core.restlet.ApplicationContextId;
import de.twenty11.skysail.server.core.restlet.RouteBuilder;
import de.twenty11.skysail.server.services.MenuItem;
import de.twenty11.skysail.server.services.MenuItemProvider;

@Component(immediate = true)
@javax.annotation.Generated(value = "de.twenty11.skysail.server.ext.apt.GenerateSkysailApplicationProcessor")
public class SchedulerGen extends SkysailApplication implements ApplicationProvider, MenuItemProvider {

	private static final String APP_NAME = "SchedulerGen";

	public SchedulerGen() {
		super(APP_NAME);
		addToAppContext(ApplicationContextId.IMG, "/static/img/silk/page_link.png");
	}

	@Override
	protected void attach() {
	    // Application root resource
		router.attach(new RouteBuilder("", RootResource.class));

		router.attach(new RouteBuilder("", PostFolderResource.class));
		router.attach(new RouteBuilder("/Schedules/", PostScheduleResource.class));
		router.attach(new RouteBuilder("", PostCompanyResource.class));
		router.attach(new RouteBuilder("", PostContactResource.class));
		router.attach(new RouteBuilder("/Jobs/", PostJobResource.class));
		router.attach(new RouteBuilder("", PostTodoResource.class));

		router.attach(new RouteBuilder("", CompanysResource.class));
		router.attach(new RouteBuilder("", FoldersResource.class));
		router.attach(new RouteBuilder("/Schedules", SchedulesResource.class));
		router.attach(new RouteBuilder("", TodosResource.class));
		router.attach(new RouteBuilder("", ContactsResource.class));
		router.attach(new RouteBuilder("/Jobs", JobsResource.class));

		router.attach(new RouteBuilder("/Schedules/{id}", ScheduleResource.class));
		router.attach(new RouteBuilder("/Companys/{id}", CompanyResource.class));
		router.attach(new RouteBuilder("/Contacts/{id}", ContactResource.class));
		router.attach(new RouteBuilder("/Todos/{id}", TodoResource.class));
		router.attach(new RouteBuilder("/Folders/{id}", FolderResource.class));
		router.attach(new RouteBuilder("/Jobs/{id}", JobResource.class));

		router.attach(new RouteBuilder("", PutTodoResource.class));
		router.attach(new RouteBuilder("", PutFolderResource.class));
		router.attach(new RouteBuilder("", PutCompanyResource.class));
		router.attach(new RouteBuilder("/Schedules/{id}/", PutScheduleResource.class));
		router.attach(new RouteBuilder("", PutContactResource.class));
		router.attach(new RouteBuilder("/Jobs/{id}/", PutJobResource.class));

	}

	public List<MenuItem> getMenuEntries() {
		MenuItem appMenu = new MenuItem("SchedulerGen", "/SchedulerGen", this);
		appMenu.setCategory(MenuItem.Category.APPLICATION_MAIN_MENU);
		return Arrays.asList(appMenu);
	}

}