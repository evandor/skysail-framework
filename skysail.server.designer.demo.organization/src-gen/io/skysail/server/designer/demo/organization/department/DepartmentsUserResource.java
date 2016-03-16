package io.skysail.server.designer.demo.organization.department;
import java.util.List;

import io.skysail.api.links.Link;
import io.skysail.api.responses.SkysailResponse;
import io.skysail.server.restlet.resources.EntityServerResource;

import io.skysail.server.designer.demo.organization.department.*;
import io.skysail.server.designer.demo.organization.department.resources.*;
import io.skysail.server.designer.demo.organization.user.*;
import io.skysail.server.designer.demo.organization.user.resources.*;


/**
 * generated from targetRelationResource.stg
 */
public class DepartmentsUserResource extends EntityServerResource<User> {

    @Override
    public SkysailResponse<?> eraseEntity() {
        return null;
    }

    @Override
    public User getEntity() {
        return null;
    }

    @Override
    public List<Link> getLinks() {
        return super.getLinks(DepartmentsUsersResource.class, PostDepartmentsUserRelationResource.class);
    }

}