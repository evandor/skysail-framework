package de.twenty11.skysail.server.app.tutorial.model2rest.step6;

import java.util.*;
import java.util.concurrent.atomic.AtomicInteger;

import org.apache.commons.collections4.queue.CircularFifoQueue;

public class Step6ModelsRepository {

    private static Step6ModelsRepository instance;

    private AtomicInteger id = new AtomicInteger(0);

    public static synchronized Step6ModelsRepository getInstance() {
        if (instance == null) {
            instance = new Step6ModelsRepository();
        }
        return instance;
    }

    private CircularFifoQueue<TodoModel6> models = new CircularFifoQueue<>(5);

    private Step6ModelsRepository() {
    }

    public int add(TodoModel6 entity) {
        int newId = id.getAndIncrement();
        //entity.setId(newId);
        models.add(entity);
        return newId;
    }

    public TodoModel6 getById(int id) {
        return models.get(id);
    }

    public List<TodoModel6> findAll() {
        List<TodoModel6> result = new ArrayList<>();
        result.addAll(models);
        return result;
    }
}
