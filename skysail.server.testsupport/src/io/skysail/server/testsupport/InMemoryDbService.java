package io.skysail.server.testsupport;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.beanutils.BeanUtils;
import org.codehaus.jettison.json.JSONObject;

import de.twenty11.skysail.server.core.db.GraphDbService;

public class InMemoryDbService implements GraphDbService {

    Map<String, Map<String, Object>> db = new HashMap<>();

    @Override
    public <T> Object persist(T entity, String... edges) {
        String identifier = entity.getClass().getSuperclass().getSimpleName();
        Map<String, Object> map = db.get(identifier);
        if (map == null) {
            map = new HashMap<String, Object>();
        }
        Method method;
        try {
            method = entity.getClass().getMethod("getId");
            Object invoke = method.invoke(entity);
            map.put((String) invoke, entity);
            db.put(identifier, map);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    @Override
    public <T> JSONObject find(Class<?> cls, String id) {
        Map<String, Object> map = db.get(cls.getSimpleName());
        if (map == null) {
            return null;
        }
        Object object = map.get(id);
        try {
            return new JSONObject(BeanUtils.describe(object));
        } catch (IllegalAccessException | InvocationTargetException | NoSuchMethodException e) {
            e.printStackTrace();
        }
        return null;
    }

    @Override
    public List<String> getAll(Class<?> cls, String username) {
        return null;
    }

    @Override
    public List<Map<String, Object>> getAllAsMap(Class<?> cls, String username) {
        return null;
    }

    @Override
    public void update(JSONObject json) {
    }

}
