package io.skysail.server.db.validators;

import java.util.HashMap;
import java.util.Map;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

import org.osgi.service.component.annotations.Component;
import org.osgi.service.component.annotations.Reference;
import org.osgi.service.component.annotations.ReferenceCardinality;
import org.osgi.service.component.annotations.ReferencePolicy;

import io.skysail.domain.Nameable;
import io.skysail.server.db.DbClassName;
import io.skysail.server.db.DbService;

/**
 * Validates if an entity with the same name exists for the same parent entity.
 */
@Component(immediate = true)
public class UniqueNameValidator implements ConstraintValidator<UniqueName, Nameable> {

        private static DbService dbService;
        
        private Class<? extends Nameable> entityClass;

        @Override
        public void initialize(UniqueName uniqueNameForParent) {
            entityClass = uniqueNameForParent.entityClass();
        }

        @Override
        public boolean isValid(Nameable nameable, ConstraintValidatorContext context) {
            String sql = "SELECT from " + DbClassName.of(entityClass) + " WHERE name=:name";
            if (nameable.getId() != null) {
                sql += " AND id <> :id";
            }
            Map<String,Object> params = new HashMap<>();
            params.put("name", nameable.getName());
            params.put("id", nameable.getId());
            //params.put("owner", SecurityUtils.getSubject().getPrincipal().toString());
            return dbService.findGraphs(entityClass, sql, params).isEmpty();
        }


        @Reference(policy = ReferencePolicy.DYNAMIC, cardinality = ReferenceCardinality.MANDATORY)
        public void setDbService(DbService dbService) {
            UniqueNameValidator.dbService = dbService;
        }

        public void unsetDbService(DbService dbService) { // NOSONAR
            UniqueNameValidator.dbService = null; // NOSONAR
        }
        

    }
