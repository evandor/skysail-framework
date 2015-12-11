package io.skysail.server.designer.checklist;


import org.osgi.service.component.annotations.*;

import io.skysail.api.repos.DbRepository;
import io.skysail.server.db.*;

@Component(immediate = true, property = "name=TemplatesRepository")
public class TemplateRepository extends GraphDbRepository<io.skysail.server.designer.checklist.Template> implements DbRepository {

    @Reference
    public void setDbService(DbService dbService) {
        this.dbService = dbService;
    }

    public void unsetDbService(DbService dbService) {
        this.dbService = null;
    }

    @Activate
    public void activate() {
        //log.debug("activating io.skysail.server.designer.checklist.Template" Repository);
        dbService.createWithSuperClass("V", DbClassName.of(io.skysail.server.designer.checklist.Template.class));
        dbService.register(Template.class);
    }

}