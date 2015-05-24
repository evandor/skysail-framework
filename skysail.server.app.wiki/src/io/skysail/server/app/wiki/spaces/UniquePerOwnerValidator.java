package io.skysail.server.app.wiki.spaces;

import io.skysail.server.db.DbService2;

import java.util.*;

import javax.validation.*;

import org.apache.shiro.SecurityUtils;

import aQute.bnd.annotation.component.*;

@Component(immediate = true)
public class UniquePerOwnerValidator implements ConstraintValidator<UniquePerOwner, Space> {

    private static DbService2 dbService;
    
    @Override
    public void initialize(UniquePerOwner uniquePerOwner) {
    }

    @Override
    public boolean isValid(Space entity, ConstraintValidatorContext context) {
        String sql;
        if (entity.getId() != null) {
            sql = "SELECT from "+entity.getClass().getSimpleName()+" WHERE owner= :owner AND name= :name AND id <> :id";
        } else {
            sql = "SELECT from "+entity.getClass().getSimpleName()+" WHERE owner= :owner AND name= :name";
        }
        Map<String,Object> params = new HashMap<>();
        params.put("name", entity.getName());
        params.put("id", entity.getId());
        params.put("owner", SecurityUtils.getSubject().getPrincipal().toString());
        List<Object> findObjects = dbService.findObjects(sql, params);
        return findObjects.size() == 0;
    }

    @Reference(dynamic = true, multiple = false, optional = false)
    public void setDbService(DbService2 dbService) {
        UniquePerOwnerValidator.dbService = dbService;
    }

    public void unsetDbService(DbService2 dbService) {
        UniquePerOwnerValidator.dbService = null;
    }
}
