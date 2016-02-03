//package io.skysail.server.app.designer.application.validation;
//
//import java.util.HashMap;
//import java.util.List;
//import java.util.Map;
//
//import javax.validation.ConstraintValidator;
//import javax.validation.ConstraintValidatorContext;
//
//import org.apache.shiro.SecurityUtils;
//import org.osgi.service.component.annotations.Component;
//import org.osgi.service.component.annotations.Reference;
//import org.osgi.service.component.annotations.ReferenceCardinality;
//import org.osgi.service.component.annotations.ReferencePolicy;
//
//import io.skysail.server.app.designer.application.DbApplication;
//import io.skysail.server.db.DbClassName;
//import io.skysail.server.db.DbService;
//
//@Component(immediate = true)
//public class UniqueNameValidator implements ConstraintValidator<UniqueName, DbApplication> {
//
//        private static DbService dbService;
//
//        @Override
//        public void initialize(UniqueName uniquePerOwner) {
//            // nothing to do
//        }
//
//        @Override
//        public boolean isValid(DbApplication app, ConstraintValidatorContext context) {
//            String sql;
//            if (app.getId() != null) {
//                sql = "SELECT from " + DbClassName.of(DbApplication.class) + " WHERE name=:name AND id <> :id";
//            } else {
//                sql = "SELECT from " + DbClassName.of(DbApplication.class) + " WHERE name=:name";
//            }
//            Map<String,Object> params = new HashMap<>();
//            params.put("name", app.getName());
//            params.put("id", app.getId());
//            params.put("owner", SecurityUtils.getSubject().getPrincipal().toString());
//            List<DbApplication> findObjects = dbService.findGraphs(DbApplication.class, sql, params);
//            return findObjects.isEmpty();
//        }
//
//        @Reference(policy = ReferencePolicy.DYNAMIC, cardinality = ReferenceCardinality.MANDATORY)
//        public void setDbService(DbService dbService) {
//            UniqueNameValidator.dbService = dbService;
//        }
//
//        public void unsetDbService(DbService dbService) {
//            UniqueNameValidator.dbService = null;
//        }
//    }
