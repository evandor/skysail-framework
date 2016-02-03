package io.skysail.server.db.validators;

import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.Map;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

import org.osgi.service.component.annotations.Component;
import org.osgi.service.component.annotations.Reference;
import org.osgi.service.component.annotations.ReferenceCardinality;
import org.osgi.service.component.annotations.ReferencePolicy;
import org.restlet.engine.util.StringUtils;

import io.skysail.domain.Identifiable;
import io.skysail.domain.Nameable;
import io.skysail.server.db.DbClassName;
import io.skysail.server.db.DbService;
import lombok.extern.slf4j.Slf4j;

/**
 * Validates if an entity with the same name exists for the same parent entity.
 */
@Component(immediate = true)
@Slf4j
public class UniqueNameValidator implements ConstraintValidator<UniqueName, Nameable> {

        private static DbService dbService;
        
        private Class<? extends Nameable> entityClass;

        private String parentGetterName;

        private String relationName;

        @Override
        public void initialize(UniqueNameForParent uniqueNameForParent) {
            entityClass = uniqueNameForParent.entityClass();
            parentGetterName = determineParentGetter(uniqueNameForParent.parent());
            relationName = uniqueNameForParent.relationName();
        }

        @Override
        public boolean isValid(Nameable nameable, ConstraintValidatorContext context) {
            String parentId = getParentId(nameable);
            String sql = "SELECT from " + DbClassName.of(entityClass) + " WHERE name=:name";
            if (parentId != null) {
                sql += " AND "+parentId+" IN in('"+relationName+"')";
            }
            if (nameable.getId() != null) {
                sql += " AND id <> :id";
            }
            Map<String,Object> params = new HashMap<>();
            params.put("name", nameable.getName());
            params.put("id", nameable.getId());
            //params.put("owner", SecurityUtils.getSubject().getPrincipal().toString());
            return dbService.findGraphs(entityClass, sql, params).isEmpty();
        }

        private String getParentId(Nameable dbEntity) {
            if (parentGetterName == null) {
                return null;
            }
            Method method;
            try {
                method = dbEntity.getClass().getMethod(parentGetterName);
                return ((Identifiable)method.invoke(dbEntity)).getId();
            } catch (Exception  e) {
                log.error(e.getMessage(), e);
                return null;
            }
        }

        @Reference(policy = ReferencePolicy.DYNAMIC, cardinality = ReferenceCardinality.MANDATORY)
        public void setDbService(DbService dbService) {
            UniqueNameValidator.dbService = dbService;
        }

        public void unsetDbService(DbService dbService) { // NOSONAR
            UniqueNameValidator.dbService = null; // NOSONAR
        }
        
        private static String determineParentGetter(String parent) {
            if (StringUtils.isNullOrEmpty(parent)) {
                return null;
            }
            return "get" + parent.substring(0,1).toUpperCase() + parent.substring(1);
        }


    }
