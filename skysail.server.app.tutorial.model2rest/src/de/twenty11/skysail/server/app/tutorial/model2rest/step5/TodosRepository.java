package de.twenty11.skysail.server.app.tutorial.model2rest.step5;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.atomic.AtomicInteger;

public class TodosRepository {

    private static TodosRepository instance;
    
    private AtomicInteger id = new AtomicInteger(0);

    public static synchronized TodosRepository getInstance() {
        if (instance == null) {
            instance = new TodosRepository();
        }
        return instance;
    }

    private Map<Integer, TodoModel> models = createLRUMap(5);

    private TodosRepository() {
    }
    
    @SuppressWarnings("serial")
	private static Map<Integer, TodoModel> createLRUMap(final int maxEntries) {
        return new LinkedHashMap<Integer, TodoModel>(maxEntries*3/2, 0.7f, true) {
            @Override
            protected boolean removeEldestEntry(Map.Entry<Integer, TodoModel> eldest) {
                return size() > maxEntries;
            }
        };
    }

    public int add(TodoModel entity) {
        int newId = id.getAndIncrement();
        entity.setId(newId);
        models.put(newId, entity);
        return newId;
    }

    public TodoModel getById(int id) {
        return models.get(id);
    }

    public List<TodoModel> findAll() {
    	return new ArrayList<TodoModel>(models.values());
    }

	public void delete(Integer id) {
		models.remove(id);
	}
}
