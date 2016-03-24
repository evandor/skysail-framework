package io.skysail.server.designer.demo.organization;


import org.osgi.service.component.annotations.*;

import io.skysail.domain.core.repos.DbRepository;
import io.skysail.server.db.*;

/**
 * generated from repository.stg
 */
@Component(immediate = true, property = "name=DepartmentsRepository")
public class DepartmentRepository extends GraphDbRepository<io.skysail.server.designer.demo.organization.department.Department> implements DbRepository {

    @Reference
    public void setDbService(DbService dbService) {
        this.dbService = dbService;
    }

    public void unsetDbService(DbService dbService) {
        this.dbService = null;
    }

    @Activate
    public void activate() {
        //log.debug("activating io.skysail.server.designer.demo.organization.department.Department" Repository);
        dbService.createWithSuperClass("V", DbClassName.of(io.skysail.server.designer.demo.organization.department.Department.class));
        dbService.register(io.skysail.server.designer.demo.organization.department.Department.class);
    }

}