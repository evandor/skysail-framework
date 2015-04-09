package io.skysail.server.app.wiki.spaces;

import io.skysail.server.app.designer.DesignerApplication;
import io.skysail.server.app.designer.repo.DesignerRepository;

import java.util.Set;

import de.twenty11.skysail.server.beans.DynamicEntity;
import de.twenty11.skysail.server.beans.EntityDynaProperty;

public class Space extends DynamicEntity {

    private static DesignerRepository repo;

    public Set<EntityDynaProperty> getProperties() {
        return DesignerApplication.getProperties(repo, getBeanName(), "12:2");
    }
    
    public static void inject(DesignerRepository designerRepo) {
        repo = designerRepo;
    }

}
