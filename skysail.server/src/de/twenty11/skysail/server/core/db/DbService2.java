package de.twenty11.skysail.server.core.db;

import java.util.List;
import java.util.Map;

import aQute.bnd.annotation.ProviderType;

import com.orientechnologies.orient.core.metadata.schema.OType;

@ProviderType
public interface DbService2 {

    /**
     * takes an entity of type T and persists it as a vertex.
     * 
     * @param entity
     *            the entity to become a vertex
     * @param edges
     *            list of names of fields which should be handled as (outgoing)
     *            edges
     * @return to be done
     */
    <T> Object persist(T entity, String... edges);

    /**
     * retrieves an entity of type T identified by its id.
     * 
     * @param cls
     * @param id
     * @return
     */
    <T> T findObjectById(Class<?> cls, String id);

    // List<String> getAll(Class<?> cls, String username);

    <T> List<T> findObjects(String sql);

    <T> List<T> findObjects(String sql, Map<String, Object> params);

    <T> void update(Object id, T entity);

    /**
     * creates a vertex class in the database if not existent yet for all
     * provided vertices names.
     */
    void setupVertices(String... vertices);

    void register(Class<?>... classes);

    void delete(Class<?> cls, String id);

    void createProperty(String simpleName, String string, OType date);

    long getCount(String sql, Map<String, Object> params);

    void executeUpdate(String sql, Map<String, Object> params);

}
