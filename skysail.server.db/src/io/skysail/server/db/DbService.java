package io.skysail.server.db;

import io.skysail.api.domain.Identifiable;

import java.util.*;

import aQute.bnd.annotation.ProviderType;

import com.orientechnologies.orient.core.metadata.schema.OType;
import com.orientechnologies.orient.core.record.impl.ODocument;

@ProviderType
public interface DbService {

    // general
    Class<?> getRegisteredClass(String classname);
    void createUniqueIndex(Class<?> cls, String... columnNames);
    void createWithSuperClass(String superClass, String... vertices);
    void createEdges(String... vertices);
    void register(Class<?>... classes);
    void createProperty(String simpleName, String string, OType date);

    Object update(Object id, Identifiable entity, String... edges);

    // object api
    <T> T findObjectById(Class<?> cls, String id);
    <T> List<T> findObjects(String sql);
    <T> List<T> findObjects(String sql, Map<String, Object> params);

    // --- graph api ---
    Object persist(Identifiable entity, String... edges);
    <T> List<T> findGraphs(String sql);
    <T> List<T> findGraphs(String sql, Map<String, Object> params);
    <T> List<T> findWithGraph(String sql, Class<?> cls, Map<String, Object> params);
    void deleteVertex(String id);
    <T> T findById(Class<?> cls, String id);
    <T> T findById2(Class<?> cls, String id);

    void delete(Class<?> cls, String id);
    Object executeUpdate(String sql, Map<String, Object> params);
    void update(ODocument doc);
    Object executeUpdateVertex(String sql, Map<String, Object> params);

    void update(Map<String, Object> space);
    List<Map<String,Object>> findDocuments(String sql);
    List<Map<String,Object>> findDocuments(String sql, Map<String, Object> params);
    long getCount(String sql, Map<String, Object> params);

}
