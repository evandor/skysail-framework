package io.skysail.server.testsupport;

import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.codehaus.jettison.json.JSONObject;

import de.twenty11.skysail.server.core.db.DbService2;

public class InMemoryDbService implements DbService2 {

    /**
     * Map ClassIdentifier -> ( Map Id -> Entity)
     */
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
    public List<String> getAll(Class<?> cls, String username) {
        Map<String, Object> map = db.get(cls.getSimpleName());
        return map.values().stream().map(o -> o.toString()).collect(Collectors.toList());
    }

    @Override
    public List<Map<String, Object>> getAllAsMap(Class<?> cls, String username) {
        return null;
    }

    @Override
    public void update(JSONObject json) {
    }

    @Override
    public <T> T findObjectById(Class<?> cls, String id) {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public <T> List<T> findObjects(Class<?> cls, String username) {
        return null;
    }

    @Override
    public void setupVertices(String... vertices) {
    }

}
