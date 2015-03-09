package io.skysail.server.app.designer.repo;

import io.skysail.server.app.designer.application.Application;

import java.util.List;

import aQute.bnd.annotation.component.Activate;
import aQute.bnd.annotation.component.Component;
import aQute.bnd.annotation.component.Reference;
import de.twenty11.skysail.server.core.db.DbRepository;
import de.twenty11.skysail.server.core.db.DbService2;

@Component(immediate = true, properties = "name=DesignerRepository")
public class DesignerRepository implements DbRepository {

    private static DbService2 dbService;

    @Activate
    public void activate() {
        dbService.setupVertices(Application.class.getSimpleName());
    }

    @Reference
    public void setDbService(DbService2 dbService) {
        DesignerRepository.dbService = dbService;
    }

    public void unsetDbService(DbService2 dbService) {
        DesignerRepository.dbService = null;
    }

    public <T> List<T> findAll(Class<T> cls) {
        return dbService.findObjects(cls, "username");
    }

    public static Object add(Object entity, String... edges) {
        return dbService.persist(entity, edges);
    }

    public <T> T getById(Class<?> cls, String id) {
        return dbService.findObjectById(cls, id);
    }

    public void update(Application entity) {
        dbService.update(entity.getId(), entity);
    }

}
