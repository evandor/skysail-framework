package io.skysail.server.designer.demo.organization.department.resources;

import io.skysail.api.responses.SkysailResponse;
import io.skysail.server.restlet.resources.PutEntityServerResource;

import java.util.Date;
import org.restlet.resource.ResourceException;
import io.skysail.server.designer.demo.organization.*;
import io.skysail.server.designer.demo.organization.department.*;

/**
 * generated from putResource.stg
 */
public class PutDepartmentResourceGen extends PutEntityServerResource<io.skysail.server.designer.demo.organization.department.Department> {


    protected String id;
    protected OrganizationApplication app;

    @Override
    protected void doInit() throws ResourceException {
        id = getAttribute("id");
        app = (OrganizationApplication)getApplication();
    }

    @Override
    public void updateEntity(Department  entity) {
        io.skysail.server.designer.demo.organization.department.Department original = getEntity();
        copyProperties(original,entity);

        app.getRepository(io.skysail.server.designer.demo.organization.department.Department.class).update(original,app.getApplicationModel());
    }

    @Override
    public io.skysail.server.designer.demo.organization.department.Department getEntity() {
        return (io.skysail.server.designer.demo.organization.department.Department)app.getRepository(io.skysail.server.designer.demo.organization.department.Department.class).findOne(id);
    }

    @Override
    public String redirectTo() {
        return super.redirectTo(DepartmentsResourceGen.class);
    }
}