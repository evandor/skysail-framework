package de.twenty11.skysail.server.app.tutorial.model2rest.step3;

import java.util.*;
import java.util.concurrent.atomic.AtomicInteger;

import org.apache.commons.collections4.queue.CircularFifoQueue;

public class Step3ModelsRepository {

    private static Step3ModelsRepository instance;

    private AtomicInteger id = new AtomicInteger(0);

    public static synchronized Step3ModelsRepository getInstance() {
        if (instance == null) {
            instance = new Step3ModelsRepository();
        }
        return instance;
    }

    private CircularFifoQueue<TodoModel3> models = new CircularFifoQueue<>(5);

    private Step3ModelsRepository() {
    }

    public int add(TodoModel3 entity) {
        int newId = id.getAndIncrement();
        //entity.setId(newId);
        models.add(entity);
        return newId;
    }

    public TodoModel3 getById(int id) {
        return models.get(id);
    }

    public List<TodoModel3> findAll() {
        List<TodoModel3> result = new ArrayList<>();
        result.addAll(models);
        return result;
    }
}
