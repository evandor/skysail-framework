package io.skysail.server.app.todos.lists;

import io.skysail.server.app.todos.TodoList;
import io.skysail.server.db.DbService;

import java.util.*;

import javax.validation.*;

import org.apache.shiro.SecurityUtils;

import aQute.bnd.annotation.component.*;

@Component(immediate = true)
public class UniquePerOwnerValidator implements ConstraintValidator<UniquePerOwner, TodoList> {

    private static DbService dbService;

    @Override
    public void initialize(UniquePerOwner uniquePerOwner) {
    }

    @Override
    public boolean isValid(TodoList todoList, ConstraintValidatorContext context) {
        String sql;
        if (todoList.getId() != null) {
            sql = "SELECT from " + TodoList.class.getName().replace(".", "_") + " WHERE owner= :owner AND name= :name AND id <> :id";
        } else {
            sql = "SELECT from " + TodoList.class.getName().replace(".", "_") + " WHERE owner= :owner AND name= :name";
        }
        Map<String,Object> params = new HashMap<>();
        params.put("name", todoList.getName());
        params.put("id", todoList.getId());
        params.put("owner", SecurityUtils.getSubject().getPrincipal().toString());
        List<Object> findObjects = dbService.findGraphs(TodoList.class,sql, params);
        return findObjects.isEmpty();
    }

    @Reference(dynamic = true, multiple = false, optional = false)
    public void setDbService(DbService dbService) {
        UniquePerOwnerValidator.dbService = dbService;
    }

    public void unsetDbService(DbService dbService) {
        UniquePerOwnerValidator.dbService = null;
    }
}
