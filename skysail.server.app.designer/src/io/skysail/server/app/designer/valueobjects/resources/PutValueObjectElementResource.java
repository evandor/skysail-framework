package io.skysail.server.app.designer.valueobjects.resources;

import java.util.List;

import org.restlet.resource.ResourceException;

import io.skysail.server.app.designer.DesignerApplication;
import io.skysail.server.app.designer.repo.DesignerRepository;
import io.skysail.server.app.designer.valueobjects.DbValueObject;
import io.skysail.server.app.designer.valueobjects.DbValueObjectElement;
import io.skysail.server.model.TreeStructure;
import io.skysail.server.restlet.resources.PutEntityServerResource;

public class PutValueObjectElementResource extends PutEntityServerResource<DbValueObjectElement> {

    private DesignerApplication app;
    private DesignerRepository repository;

    @Override
    protected void doInit() throws ResourceException {
        super.doInit();
        app = (DesignerApplication) getApplication();
        repository = (DesignerRepository) app.getRepository(DbValueObjectElement.class);
    }
    
    @Override
    public DbValueObjectElement getEntity() {
        DbValueObject valueObject = repository.findValueObject(getAttribute("id"));
        return valueObject.getElements().stream().filter(e -> e.getId().equals(getAttribute("eid"))).findFirst().orElse(null);
    }

    @Override
    public void updateEntity(DbValueObjectElement element) {
        System.out.println(element);
//        app.getRepository().update(element, app.getApplicationModel());
    }
    
    @Override
    public List<TreeStructure> getTreeRepresentation() {
        return app.getTreeRepresentation(getAttribute("id"));
    }

}
