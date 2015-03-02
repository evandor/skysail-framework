package de.twenty11.skysail.server.core.db;

import java.util.List;

import org.codehaus.jettison.json.JSONObject;

import aQute.bnd.annotation.ProviderType;

@ProviderType
public interface GraphDbService {

    <T> Object persist(T entity);

    <T> JSONObject find(Class<?> cls, String id);

    List<String> getAll(Class<?> cls, String username);

    void update(JSONObject json);

}
