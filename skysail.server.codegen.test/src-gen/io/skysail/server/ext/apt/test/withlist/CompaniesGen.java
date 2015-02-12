package io.skysail.server.ext.apt.test.withlist;

import java.util.Arrays;
import java.util.List;

import aQute.bnd.annotation.component.Component;
import aQute.bnd.annotation.component.Reference;
import de.twenty11.skysail.server.app.ApplicationProvider;
import de.twenty11.skysail.server.app.SkysailApplication;

import io.skysail.server.ext.apt.test.twoentities.jobs.PostJobResource;
import io.skysail.server.ext.apt.test.withlist.folders.PostFolderResource;
import io.skysail.server.ext.apt.test.simple.todos.PostTodoResource;
import io.skysail.server.ext.apt.test.withlist.companies.PostCompanyResource;
import io.skysail.server.ext.apt.test.crm.contacts.PostContactResource;
import io.skysail.server.ext.apt.test.twoentities.schedules.PostScheduleResource;

import io.skysail.server.ext.apt.test.simple.todos.PutTodoResource;
import io.skysail.server.ext.apt.test.twoentities.jobs.PutJobResource;
import io.skysail.server.ext.apt.test.twoentities.schedules.PutScheduleResource;
import io.skysail.server.ext.apt.test.withlist.companies.PutCompanyResource;
import io.skysail.server.ext.apt.test.withlist.folders.PutFolderResource;
import io.skysail.server.ext.apt.test.crm.contacts.PutContactResource;

import io.skysail.server.ext.apt.test.crm.contacts.ContactsResource;
import io.skysail.server.ext.apt.test.simple.todos.TodosResource;
import io.skysail.server.ext.apt.test.twoentities.schedules.SchedulesResource;
import io.skysail.server.ext.apt.test.withlist.folders.FoldersResource;
import io.skysail.server.ext.apt.test.twoentities.jobs.JobsResource;
import io.skysail.server.ext.apt.test.withlist.companies.CompanysResource;

import io.skysail.server.ext.apt.test.withlist.companies.CompanyResource;
import io.skysail.server.ext.apt.test.crm.contacts.ContactResource;
import io.skysail.server.ext.apt.test.twoentities.jobs.JobResource;
import io.skysail.server.ext.apt.test.twoentities.schedules.ScheduleResource;
import io.skysail.server.ext.apt.test.withlist.folders.FolderResource;
import io.skysail.server.ext.apt.test.simple.todos.TodoResource;


import de.twenty11.skysail.server.core.restlet.ApplicationContextId;
import de.twenty11.skysail.server.core.restlet.RouteBuilder;
import de.twenty11.skysail.server.services.MenuItem;
import de.twenty11.skysail.server.services.MenuItemProvider;

@Component(immediate = true)
@javax.annotation.Generated(value = "de.twenty11.skysail.server.ext.apt.GenerateSkysailApplicationProcessor")
public class CompaniesGen extends SkysailApplication implements MenuItemProvider, ApplicationProvider {

	private static final String APP_NAME = "CompaniesGen";

	public CompaniesGen() {
		super(APP_NAME);
		addToAppContext(ApplicationContextId.IMG, "/static/img/silk/page_link.png");
	}

	@Override
	protected void attach() {
	    // Application root resource
		router.attach(new RouteBuilder("", RootResource.class));

		router.attach(new RouteBuilder("", PostJobResource.class));
		router.attach(new RouteBuilder("/Folders/", PostFolderResource.class));
		router.attach(new RouteBuilder("", PostTodoResource.class));
		router.attach(new RouteBuilder("/Companys/", PostCompanyResource.class));
		router.attach(new RouteBuilder("", PostContactResource.class));
		router.attach(new RouteBuilder("", PostScheduleResource.class));

		router.attach(new RouteBuilder("", ContactsResource.class));
		router.attach(new RouteBuilder("", TodosResource.class));
		router.attach(new RouteBuilder("", SchedulesResource.class));
		router.attach(new RouteBuilder("/Folders", FoldersResource.class));
		router.attach(new RouteBuilder("", JobsResource.class));
		router.attach(new RouteBuilder("/Companys", CompanysResource.class));

		router.attach(new RouteBuilder("/Companys/{id}", CompanyResource.class));
		router.attach(new RouteBuilder("/Contacts/{id}", ContactResource.class));
		router.attach(new RouteBuilder("/Jobs/{id}", JobResource.class));
		router.attach(new RouteBuilder("/Schedules/{id}", ScheduleResource.class));
		router.attach(new RouteBuilder("/Folders/{id}", FolderResource.class));
		router.attach(new RouteBuilder("/Todos/{id}", TodoResource.class));

		router.attach(new RouteBuilder("", PutTodoResource.class));
		router.attach(new RouteBuilder("", PutJobResource.class));
		router.attach(new RouteBuilder("", PutScheduleResource.class));
		router.attach(new RouteBuilder("/Companys/{id}/", PutCompanyResource.class));
		router.attach(new RouteBuilder("/Folders/{id}/", PutFolderResource.class));
		router.attach(new RouteBuilder("", PutContactResource.class));

	}

	public List<MenuItem> getMenuEntries() {
		MenuItem appMenu = new MenuItem("CompaniesGen", "/CompaniesGen", this);
		appMenu.setCategory(MenuItem.Category.APPLICATION_MAIN_MENU);
		return Arrays.asList(appMenu);
	}

}