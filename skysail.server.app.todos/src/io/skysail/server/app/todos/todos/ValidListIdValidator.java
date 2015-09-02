package io.skysail.server.app.todos.todos;

import io.skysail.server.app.todos.TodoList;
import io.skysail.server.db.DbService;

import java.util.*;

import javax.validation.*;

import org.apache.shiro.SecurityUtils;

import aQute.bnd.annotation.component.*;

@Component(immediate = true)
public class ValidListIdValidator implements ConstraintValidator<ValidListId, String> {

    private static DbService dbService;

    @Override
    public void initialize(ValidListId listId) {
    }

    @Override
    public boolean isValid(String todoList, ConstraintValidatorContext context) {
        if (todoList == null) {
            return false;
        }
        String sql = "SELECT from TodoList WHERE @rid= :id";
        Map<String, Object> params = new HashMap<>();
        params.put("id", todoList.replace("#", ""));
        String owner = SecurityUtils.getSubject().getPrincipal().toString();
        List<TodoList> findObjects = dbService.findObjects(sql, params);
        return findObjects.size() == 1 && findObjects.get(0).getOwner().equals(owner);
    }

    @Reference(dynamic = true, multiple = false, optional = false)
    public void setDbService(DbService dbService) {
        ValidListIdValidator.dbService = dbService;
    }

    public void unsetDbService(DbService dbService) { // NO_UCD
        ValidListIdValidator.dbService = null;
    }
}
