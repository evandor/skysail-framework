package io.skysail.server.db;

import io.skysail.api.domain.Identifiable;

import java.util.*;

import aQute.bnd.annotation.ProviderType;

import com.orientechnologies.orient.core.metadata.schema.OType;
import com.orientechnologies.orient.core.record.impl.ODocument;

@ProviderType
public interface DbService {

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
    Object persist(Identifiable entity, String... edges);

    Object update(Object id, Identifiable entity, String... edges);

    /**
     * retrieves an entity of type T identified by its id.
     *
     * @param cls
     * @param id
     * @return
     */
    <T> T findObjectById(Class<?> cls, String id);
    <T> T findById(Class<?> cls, String id);
    <T> List<T> findObjects(String sql);
    List<Map<String,Object>> findDocuments(String sql);
    <T> List<T> findGraphs(String sql);
    <T> List<T> findObjects(String sql, Map<String, Object> params);
    List<Map<String,Object>> findDocuments(String sql, Map<String, Object> params);
    <T> List<T> findGraphs(String sql, Map<String, Object> params);
    <T> List<T> findWithGraph(String sql, Class<?> cls, Map<String, Object> params);


    /**
     * creates a vertex class in the database if not existent yet for all
     * provided vertices names.
     */
    void createWithSuperClass(String superClass, String... vertices);

    void createEdges(String... vertices);

    void register(Class<?>... classes);

    Class<?> getRegisteredClass(String classname);

    void delete(Class<?> cls, String id);
    void deleteVertex(String id);

    void createProperty(String simpleName, String string, OType date);

    long getCount(String sql, Map<String, Object> params);

    Object executeUpdate(String sql, Map<String, Object> params);

    void createUniqueIndex(Class<?> cls, String... columnNames);

    void update(Map<String, Object> space);

    void update(ODocument doc);

    Object executeUpdateVertex(String sql, Map<String, Object> params);


}
