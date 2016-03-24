package io.skysail.server.app.designer.valueobjects.resources;

import org.restlet.resource.ResourceException;

import io.skysail.server.app.designer.DesignerApplication;
import io.skysail.server.app.designer.application.DbApplication;
import io.skysail.server.app.designer.repo.DesignerRepository;
import io.skysail.server.app.designer.valueobjects.DbValueObject;
import io.skysail.server.restlet.resources.PutEntityServerResource;

public class PutValueObjectResource extends PutEntityServerResource<DbValueObject> {

    private DesignerApplication app;
    private DesignerRepository repo;

    @Override
    protected void doInit() throws ResourceException {
        super.doInit();
        app = (DesignerApplication) getApplication();
        repo = (DesignerRepository) app.getRepository(DbApplication.class);
      }

    @Override
    public DbValueObject getEntity() {
        return repo.findValueObject(getAttribute("id"));
    }

    @Override
    public void updateEntity(DbValueObject entity) {
        super.updateEntity(entity);
    }
//    public SkysailResponse<DbValueObject> updateEntity(DbValueObject entity) {
//        //app.getRepository().update(entity);
//        return new SkysailResponse<String>();
//     }

}
