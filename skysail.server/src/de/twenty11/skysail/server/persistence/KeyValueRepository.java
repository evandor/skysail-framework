package de.twenty11.skysail.server.persistence;

import java.util.List;

import de.twenty11.skysail.api.domain.Identifiable;


public interface KeyValueRepository<T extends Identifiable> {

	void add(T entity);

	List<T> getAll(Class<T> clz);

    T get(Class<T> cls, String id);

	KeyValueRepository<T> withEntity(Class<T> cls);

    void saveOrUpdate(T app);


}
