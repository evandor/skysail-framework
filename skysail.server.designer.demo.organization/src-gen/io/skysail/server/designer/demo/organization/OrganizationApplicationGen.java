package io.skysail.server.designer.demo.organization;

import java.util.*;

import org.osgi.service.component.annotations.*;
import org.osgi.service.event.EventAdmin;

import io.skysail.server.app.*;
import de.twenty11.skysail.server.core.restlet.*;
import io.skysail.domain.Identifiable;
import io.skysail.domain.core.Repositories;
import io.skysail.server.app.*;
import io.skysail.server.menus.MenuItemProvider;

import io.skysail.server.designer.demo.organization.*;

import io.skysail.server.designer.demo.organization.department.*;
import io.skysail.server.designer.demo.organization.department.resources.*;
import io.skysail.server.designer.demo.organization.user.*;
import io.skysail.server.designer.demo.organization.user.resources.*;


/**
 * generated from application.stg
 */
public class OrganizationApplicationGen extends SkysailApplication implements ApplicationProvider, MenuItemProvider {

    public static final String LIST_ID = "lid";
    public static final String TODO_ID = "id";
    public static final String APP_NAME = "Organization";

    @Reference(cardinality = ReferenceCardinality.OPTIONAL)
    private volatile EventAdmin eventAdmin;

    public OrganizationApplicationGen(String name, ApiVersion apiVersion, List<Class<? extends Identifiable>>  entityClasses) {
        super(name, apiVersion, entityClasses);
    }

    @Reference(policy = ReferencePolicy.DYNAMIC, cardinality = ReferenceCardinality.MANDATORY)
    public void setRepositories(Repositories repos) {
        super.setRepositories(repos);
    }

    public void unsetRepositories(Repositories repo) {
        super.setRepositories(null);
    }



    @Override
    protected void attach() {
        super.attach();
        router.attach(new RouteBuilder("/Departments/{id}", DepartmentResourceGen.class));
        router.attach(new RouteBuilder("/Departments/", PostDepartmentResourceGen.class));
        router.attach(new RouteBuilder("/Departments/{id}/", PutDepartmentResourceGen.class));
        router.attach(new RouteBuilder("/Departments", DepartmentsResourceGen.class));
        router.attach(new RouteBuilder("", io.skysail.server.designer.demo.organization.department.resources.DepartmentsResourceGen.class));
        router.attach(new RouteBuilder("/Departments/{id}/Users", DepartmentsUsersResource.class));
        router.attach(new RouteBuilder("/Departments/{id}/Users/", PostDepartmentToNewUserRelationResource.class));
        router.attach(new RouteBuilder("/Departments/{id}/Users/{targetId}", DepartmentsUserResource.class));
        router.attach(new RouteBuilder("/Users/{id}", UserResourceGen.class));
        router.attach(new RouteBuilder("/Users/", PostUserResourceGen.class));
        router.attach(new RouteBuilder("/Users/{id}/", PutUserResourceGen.class));
        router.attach(new RouteBuilder("/Users", UsersResourceGen.class));
        router.attach(new RouteBuilder("", io.skysail.server.designer.demo.organization.user.resources.UsersResourceGen.class));

    }

    public EventAdmin getEventAdmin() {
        return eventAdmin;
    }

}