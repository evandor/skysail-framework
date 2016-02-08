package de.twenty11.skysail.server.mgt.worker;

import java.util.concurrent.*;

import org.osgi.service.component.annotations.*;

import io.skysail.server.services.*;

@Component(immediate = true)
public class SkysailManagerThreadPool implements SkysailThreadPool {

    private volatile CompletionService<String> pool;
    private volatile ExecutorService threadPool;
    private volatile SocketIoBroadcasting socketIoBroadcasting;

    public SkysailManagerThreadPool() {
        threadPool = Executors.newFixedThreadPool(4);
    }

    @Override
    public void start() {
        pool = new ExecutorCompletionService<String>(threadPool);
    }

    @Override
    public void stop() {
        threadPool.shutdown();
        pool = null;
    }

    @Override
    public void submit(Callable<String> task) {
        if (pool == null) {
            start();
        }
        if (socketIoBroadcasting != null) {
            socketIoBroadcasting.send("{\"userName\": \"to be done\",\"message\": \"new task "
                    + task.getClass().getName() + "\"}");
        }
        pool.submit(task);
    }

    @Override
    public String get() {
        try {
            return pool.take().get();
            // } catch (InterruptedException | ExecutionException e) {
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    @Reference(policy = ReferencePolicy.DYNAMIC, cardinality = ReferenceCardinality.OPTIONAL)
    public void setSocketIoBroadcasting(SocketIoBroadcasting socketIoBroadcasting) {
        this.socketIoBroadcasting = socketIoBroadcasting;
    }

    public void unsetSocketIoBroadcasting(SocketIoBroadcasting socketIoBroadcasting) {
        this.socketIoBroadcasting = null;
    }

}
