package de.twenty11.skysail.server.core.db;

import java.util.List;
import java.util.Map;

import aQute.bnd.annotation.ProviderType;

import com.orientechnologies.orient.core.sql.query.OSQLSynchQuery;

@ProviderType
public interface DbService {

    void register(Class<?>... entities);

    <T> T persist(T o, Class<?>... linkedClasses);

    <T> T find(String id, Class<?>... registerClasses);

    String findAndReturnJson(String id, Class<?>... registerClasses);

    <T> T findOne(String sql, Class<T> clz, Map<String, Object> params);

    <T> T find(String sql, Class<T> clz, Map<String, Object> params);

    <T> List<T> findAll(Class<T> entityClass, Class<?>... linkedClasses);

    <T> List<String> findAllAsJsonList(Class<T> entityClass, Class<?>... linkedClasses);

    <T> List<T> findAll(String sql, Class<T> clz, Map<String, Object> params);

    void delete(String id);

    <T> T update(T o);

    <T> String getId(T o);

    <T> List<T> query(OSQLSynchQuery<T> osqlSynchQuery, Class<?>... registerClasses);

}
