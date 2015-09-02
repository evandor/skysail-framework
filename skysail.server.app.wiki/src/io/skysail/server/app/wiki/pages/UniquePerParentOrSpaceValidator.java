package io.skysail.server.app.wiki.pages;

import io.skysail.server.db.DbService;

import javax.validation.*;

import aQute.bnd.annotation.component.*;

@Component(immediate = true)
public class UniquePerParentOrSpaceValidator implements ConstraintValidator<UniquePerParentOrSpace, Page> {

    private static DbService dbService;
    
    @Override
    public void initialize(UniquePerParentOrSpace uniquePerOwner) {
    }

    @Override
    public boolean isValid(Page entity, ConstraintValidatorContext context) {
//        String sql;
//        if (entity.getId() != null) {
//            sql = "SELECT from "+entity.getClass().getSimpleName()+" WHERE owner= :owner AND name= :name AND id <> :id";
//        } else {
//            sql = "SELECT from "+entity.getClass().getSimpleName()+" WHERE owner= :owner AND name= :name";
//        }
//        Map<String,Object> params = new HashMap<>();
//        params.put("name", entity.getName());
//        params.put("id", entity.getId());
//        params.put("owner", SecurityUtils.getSubject().getPrincipal().toString());
//        List<Object> findObjects = dbService.findObjects(sql, params);
//        return findObjects.size() == 0;
        return true;
    }

    @Reference(dynamic = true, multiple = false, optional = false)
    public void setDbService(DbService dbService) {
        UniquePerParentOrSpaceValidator.dbService = dbService;
    }

    public void unsetDbService(DbService dbService) {
        UniquePerParentOrSpaceValidator.dbService = null;
    }
}
