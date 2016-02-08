//package io.skysail.server.app.wiki.spaces;
//
//import java.util.*;
//
//import javax.validation.*;
//
//import org.apache.shiro.SecurityUtils;
//import org.osgi.service.component.annotations.*;
//
//import io.skysail.server.db.DbService;
//
//@Component(immediate = true)
//public class UniquePerOwnerValidator implements ConstraintValidator<UniquePerOwner, Space> {
//
//    private static DbService dbService;
//    
//    @Override
//    public void initialize(UniquePerOwner uniquePerOwner) {
//    }
//
//    @Override
//    public boolean isValid(Space entity, ConstraintValidatorContext context) {
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
//    }
//
//    @Reference(policy = ReferencePolicy.DYNAMIC, cardinality = ReferenceCardinality.MANDATORY)
//    public void setDbService(DbService dbService) {
//        UniquePerOwnerValidator.dbService = dbService;
//    }
//
//    public void unsetDbService(DbService dbService) {
//        UniquePerOwnerValidator.dbService = null;
//    }
//}
