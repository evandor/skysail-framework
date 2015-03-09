package io.skysail.server.app.wiki.spaces;

import io.skysail.server.app.designer.application.Application;
import io.skysail.server.app.designer.entities.Entity;
import io.skysail.server.app.designer.repo.DesignerRepository;

import java.util.Optional;
import java.util.Set;
import java.util.SortedSet;
import java.util.TreeSet;

import de.twenty11.skysail.server.beans.DynamicEntity;
import de.twenty11.skysail.server.beans.EntityDynaProperty;

public class Space extends DynamicEntity {

    private static DesignerRepository repo;

    public Space() {
        super("Contract", getProperties());
    }

    private static Set<EntityDynaProperty> getProperties() {
        SortedSet<EntityDynaProperty> properties = new TreeSet<>();

        Application designerApplication = repo.getById(Application.class, "21:2");
        Optional<Entity> entity = designerApplication.getEntities().stream().filter(e -> {
            return "Space".equals(e.getName());
        }).findFirst();
        if (entity.isPresent()) {
            properties.add(new EntityDynaProperty(entity.get().getName(), String.class));
        }

        return properties;
    }

    public static void inject(DesignerRepository designerRepo) {
        repo = designerRepo;
    }

}
