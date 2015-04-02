package io.skysail.server.app.todos.lists;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

import org.apache.shiro.SecurityUtils;

import aQute.bnd.annotation.component.Component;
import aQute.bnd.annotation.component.Reference;
import de.twenty11.skysail.server.core.db.DbService2;

@Component(immediate = true)
public class UniquePerOwnerValidator implements ConstraintValidator<UniquePerOwner, String> {

    private static DbService2 dbService;
    private UniquePerOwner uniquePerOwner;
    
    @Override
    public void initialize(UniquePerOwner uniquePerOwner) {
        this.uniquePerOwner = uniquePerOwner;
    }

    @Override
    public boolean isValid(String todoListName, ConstraintValidatorContext context) {
        String sql = "SELECT from TodoList WHERE owner= :owner AND name= :name";
        Map<String,Object> params = new HashMap<>();
        params.put("name", todoListName);
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
