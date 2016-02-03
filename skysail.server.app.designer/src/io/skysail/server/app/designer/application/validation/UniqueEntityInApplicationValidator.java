package io.skysail.server.app.designer.application.validation;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

import org.apache.shiro.SecurityUtils;
import org.osgi.service.component.annotations.Component;
import org.osgi.service.component.annotations.Reference;
import org.osgi.service.component.annotations.ReferenceCardinality;
import org.osgi.service.component.annotations.ReferencePolicy;

import io.skysail.server.app.designer.entities.DbEntity;
import io.skysail.server.db.DbClassName;
import io.skysail.server.db.DbService;

@Component(immediate = true)
public class UniqueEntityInApplicationValidator implements ConstraintValidator<UniqueEntityInApplication, DbEntity> {

        private static DbService dbService;

        @Override
        public void initialize(UniqueEntityInApplication uniquePerOwner) {
            // nothing to do
        }

        @Override
        public boolean isValid(DbEntity dbEntity, ConstraintValidatorContext context) {
            String sql;
            if (dbEntity.getId() != null) { // where name='Source' AND #14:2 IN in('entities') 
                sql = "SELECT from " + DbClassName.of(DbEntity.class) + " WHERE name=:name  AND "+dbEntity.getDbApplication().getId()+" IN in('entities') AND id <> :id";
            } else {
                sql = "SELECT from " + DbClassName.of(DbEntity.class) + " WHERE name=:name AND "+dbEntity.getDbApplication().getId()+" IN in('entities')";
            }
            Map<String,Object> params = new HashMap<>();
            params.put("name", dbEntity.getName());
            params.put("id", dbEntity.getId());
            params.put("owner", SecurityUtils.getSubject().getPrincipal().toString());
            List<DbEntity> findObjects = dbService.findGraphs(DbEntity.class, sql, params);
            return findObjects.isEmpty();
        }

        @Reference(policy = ReferencePolicy.DYNAMIC, cardinality = ReferenceCardinality.MANDATORY)
        public void setDbService(DbService dbService) {
            UniqueEntityInApplicationValidator.dbService = dbService;
        }

        public void unsetDbService(DbService dbService) {
            UniqueEntityInApplicationValidator.dbService = null;
        }
    }
