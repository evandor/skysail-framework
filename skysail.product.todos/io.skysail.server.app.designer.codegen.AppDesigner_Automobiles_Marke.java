package io.skysail.server.app.designer.codegen;

import io.skysail.server.app.designer.DesignerApplication;
import io.skysail.server.app.designer.repo.DesignerRepository;

import java.util.Set;

import de.twenty11.skysail.server.beans.DynamicEntity;
import de.twenty11.skysail.server.beans.EntityDynaProperty;

public class AppDesigner_Automobiles_Marke extends DynamicEntity {

    private static DesignerRepository repo;

    public Set<EntityDynaProperty> getProperties() {
        return DesignerApplication.getProperties(repo, getBeanName(), "#12:4");
    }
    
    public static void inject(DesignerRepository designerRepo) {
        repo = designerRepo;
    }

}
