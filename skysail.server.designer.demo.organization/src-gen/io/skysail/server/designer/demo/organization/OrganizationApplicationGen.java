package io.skysail.server.designer.demo.organization;

import java.util.List;

import org.osgi.service.component.annotations.Reference;
import org.osgi.service.component.annotations.ReferenceCardinality;
import org.osgi.service.component.annotations.ReferencePolicy;
import org.osgi.service.event.EventAdmin;

import de.twenty11.skysail.server.core.restlet.RouteBuilder;
import io.skysail.domain.Identifiable;
import io.skysail.domain.core.Repositories;
import io.skysail.server.app.ApiVersion;
import io.skysail.server.app.ApplicationProvider;
import io.skysail.server.app.SkysailApplication;
import io.skysail.server.designer.demo.organization.department.DepartmentsUserResource;
import io.skysail.server.designer.demo.organization.department.DepartmentsUsersResource;
import io.skysail.server.designer.demo.organization.department.PostDepartmentToNewUserRelationResource;
import io.skysail.server.designer.demo.organization.department.resources.DepartmentResourceGen;
import io.skysail.server.designer.demo.organization.department.resources.DepartmentsResourceGen;
import io.skysail.server.designer.demo.organization.department.resources.PostDepartmentResourceGen;
import io.skysail.server.designer.demo.organization.department.resources.PutDepartmentResourceGen;
import io.skysail.server.designer.demo.organization.user.resources.PostUserResourceGen;
import io.skysail.server.designer.demo.organization.user.resources.PutUserResourceGen;
import io.skysail.server.designer.demo.organization.user.resources.UserResourceGen;
import io.skysail.server.designer.demo.organization.user.resources.UsersResourceGen;
import io.skysail.server.menus.MenuItemProvider;

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