package io.skysail.server.app.wiki.spaces;

import io.skysail.server.app.designer.application.Application;
import io.skysail.server.app.designer.entities.Entity;
import io.skysail.server.app.designer.fields.EntityField;
import io.skysail.server.app.designer.repo.DesignerRepository;

import java.util.List;
import java.util.Set;
import java.util.SortedSet;
import java.util.TreeSet;

import de.twenty11.skysail.server.beans.DynamicEntity;
import de.twenty11.skysail.server.beans.EntityDynaProperty;

public class Space extends DynamicEntity {

    private static DesignerRepository repo;

    public Space() {
        super("Space");
    }

    public Set<EntityDynaProperty> getProperties() {
        SortedSet<EntityDynaProperty> properties = new TreeSet<>();

        Application designerApplication = repo.getById(Application.class, "25:1");
        // System.out.println(designerApplication.getEntities().get(0).getName());
        List<Entity> entities = designerApplication.getEntities();

        // streams dont't seem to work here ?!?! (with orientdb objects)
        for (Entity entity : entities) {
            if ("Space".equals(entity.getName())) {
                List<EntityField> fields = entity.getFields();
                for (EntityField entityField : fields) {
                    properties.add(new EntityDynaProperty(entityField.getName(), String.class));
                }
                break;
            }
        }

        // System.out.println(entities.size());
        // Optional<Entity> entity = entities.stream().filter(e -> {
        // return namePredecate(e);
        // }).findFirst();
        // if (entity.isPresent()) {
        // }

        return properties;
    }

    private static boolean namePredecate(Entity e) {
        System.out.println(e);
        return "Space".equals(e.getName());
    }

    public static void inject(DesignerRepository designerRepo) {
        repo = designerRepo;
    }

}
