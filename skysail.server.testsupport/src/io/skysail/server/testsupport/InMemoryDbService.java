package io.skysail.server.testsupport;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.Collectors;

import de.twenty11.skysail.server.core.db.DbService2;

/**
 * An DbService2 implementation to be used in tests.
 * 
 * The provided entities need a method "String getId()".
 *
 */
public class InMemoryDbService implements DbService2 {

    private volatile AtomicInteger cnt = new AtomicInteger(0);

    /**
     * Map ClassIdentifier -> ( Map Id -> Entity)
     */
    Map<String, Map<String, Object>> db = new HashMap<>();

    @Override
    public <T> Object persist(T entity, String... edges) {
        String identifier = entity.getClass().getSimpleName();
        Map<String, Object> map = db.get(identifier);
        if (map == null) {
            map = new HashMap<String, Object>();
            db.put(identifier, map);
        }
        String newId = Integer.toString(cnt.incrementAndGet());

        // Method method;
        // try {
        // method = entity.getClass().getMethod("setId");
        // Object invoke = method.invoke(entity);
        // map.put((String) invoke, entity);
        // db.put(identifier, map);
        // } catch (Exception e) {
        // e.printStackTrace();
        // }
        //
        //

        map.put(newId, entity);

        return newId;
    }

    @Override
    public List<String> getAll(Class<?> cls, String username) {
        Map<String, Object> map = db.get(cls.getSimpleName());
        return map.values().stream().map(o -> o.toString()).collect(Collectors.toList());
    }

    @SuppressWarnings("unchecked")
    @Override
    public <T> T findObjectById(Class<?> cls, String id) {
        Map<String, Object> map = db.get(cls.getSimpleName());
        return (T) map.get(id);
    }

    @Override
    public <T> List<T> findObjects(Class<?> cls, String username) {
        Map<String, Object> map = db.get(cls.getSimpleName());
        return map.values().stream().map(o -> {
            return (T) o;
        }).collect(Collectors.toList());
    }

    @Override
    public void setupVertices(String... vertices) {
    }

    @Override
    public <T> void update(Object id, T entity) {
        Map<String, Object> entities = db.get(entity.getClass().getSimpleName());
        Object entityFromMemory = entities.get(id);
        if (entityFromMemory != null) {
            entities.put(id.toString(), entity);
        }

    }

    @Override
    public void register(Class<?>... classes) {
    }

    @Override
    public void delete(Class<?> cls, String id) {
        // TODO Auto-generated method stub

    }

}
