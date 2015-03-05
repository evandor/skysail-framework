package de.twenty11.skysail.server.core.db;

import java.util.List;
import java.util.Map;

import org.codehaus.jettison.json.JSONObject;

import aQute.bnd.annotation.ProviderType;

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

    <T> JSONObject find(Class<?> cls, String id);

    List<String> getAll(Class<?> cls, String username);

    <T> List<T> findObjects(Class<?> cls, String username);

    List<Map<String, Object>> getAllAsMap(Class<?> cls, String username);

    void update(JSONObject json);

}
