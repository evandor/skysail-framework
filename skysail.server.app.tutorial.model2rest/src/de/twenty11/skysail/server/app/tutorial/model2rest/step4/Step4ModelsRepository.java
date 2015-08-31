package de.twenty11.skysail.server.app.tutorial.model2rest.step4;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;

import org.apache.commons.collections4.queue.CircularFifoQueue;

public class Step4ModelsRepository {

    private static Step4ModelsRepository instance;
    
    private AtomicInteger id = new AtomicInteger(0);

    public static synchronized Step4ModelsRepository getInstance() {
        if (instance == null) {
            instance = new Step4ModelsRepository();
        }
        return instance;
    }

    private CircularFifoQueue<TodoModel4> models = new CircularFifoQueue<>(5);

    private Step4ModelsRepository() {
    }

    public int add(TodoModel4 entity) {
        int newId = id.getAndIncrement();
        entity.setId(newId);
        models.add(entity);
        return newId;
    }

    public TodoModel4 getById(int id) {
        return models.get(id);
    }

    public List<TodoModel4> findAll() {
        List<TodoModel4> result = new ArrayList<>();
        result.addAll(models);
        return result;
    }
}
