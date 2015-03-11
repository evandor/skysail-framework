package io.skysail.server.app.crm;

import io.skysail.server.app.crm.companies.Company;
import io.skysail.server.app.crm.emails.EmailRelation;

import java.util.List;

import lombok.extern.slf4j.Slf4j;
import aQute.bnd.annotation.component.Activate;
import aQute.bnd.annotation.component.Component;
import aQute.bnd.annotation.component.Reference;

import com.orientechnologies.orient.core.metadata.schema.OType;

import de.twenty11.skysail.server.beans.DynamicEntity;
import de.twenty11.skysail.server.core.db.DbRepository;
import de.twenty11.skysail.server.core.db.DbService2;

@Component(immediate = true, properties = "name=CrmRepository")
@Slf4j
public class CrmRepository implements DbRepository {

    private static DbService2 dbService;

    @Activate
    public void activate() {
        dbService.setupVertices(CrmEntity.class.getSimpleName(), DynamicEntity.class.getSimpleName(),
                EmailRelation.class.getSimpleName());
        try {
            dbService.createProperty(CrmEntity.class.getSimpleName(), "created", OType.DATE);
            dbService.createProperty(CrmEntity.class.getSimpleName(), "changed", OType.DATE);
            List<EmailRelation> emailRelations = findAll(EmailRelation.class);
            if (emailRelations.size() == 0) {
                EmailRelation.initialData().stream().forEach(data -> {
                    add(data);
                });
            }
        } catch (Exception e) {
            log.error(e.getMessage(), e);
        }
    }

    @Reference
    public void setDbService(DbService2 dbService) {
        CrmRepository.dbService = dbService;
    }

    public void unsetDbService(DbService2 dbService) {
        CrmRepository.dbService = null;
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

    public void update(Company entity) {
        // dbService.update(entity.getId(), entity);
    }

}
