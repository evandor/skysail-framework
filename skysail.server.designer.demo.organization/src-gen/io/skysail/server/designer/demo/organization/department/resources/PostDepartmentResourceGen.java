package io.skysail.server.designer.demo.organization.department.resources;

import java.util.Date;

import io.skysail.api.responses.SkysailResponse;
import io.skysail.server.ResourceContextId;
import io.skysail.server.restlet.resources.PostEntityServerResource;

import org.apache.shiro.SecurityUtils;
import org.apache.shiro.subject.Subject;
import org.restlet.resource.ResourceException;
import io.skysail.server.designer.demo.organization.*;
import io.skysail.server.designer.demo.organization.department.*;

/**
 * generated from postResource.stg
 */
public class PostDepartmentResourceGen extends PostEntityServerResource<io.skysail.server.designer.demo.organization.department.Department> {

	protected OrganizationApplication app;

    public PostDepartmentResourceGen() {
        addToContext(ResourceContextId.LINK_TITLE, "Create new ");
    }

    @Override
    protected void doInit() throws ResourceException {
        app = (OrganizationApplication) getApplication();
    }

    @Override
    public io.skysail.server.designer.demo.organization.department.Department createEntityTemplate() {
        return new Department();
    }

    @Override
    public void addEntity(io.skysail.server.designer.demo.organization.department.Department entity) {
        Subject subject = SecurityUtils.getSubject();
        String id = app.getRepository(io.skysail.server.designer.demo.organization.department.Department.class).save(entity, app.getApplicationModel()).toString();
        entity.setId(id);

    }

    @Override
    public String redirectTo() {
        return super.redirectTo(DepartmentsResourceGen.class);
    }
}