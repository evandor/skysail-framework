package io.skysail.server.http.impl;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;

/**
 * @see http
 *      ://collaboration.cmc.ec.gc.ca/science/rpn/biblio/ddj/Website/articles
 *      /DDJ/2006/0609/060801og01/060801og01.html
 *
 */
public class ThreadManager {

    /**
     * An abstract base class for use with ThreadManager. It implements
     * runnable, and derived class should implement the run() method. It also
     * defines a method called interrupt() that derived class must implement
     * such that, when called, it will cause the run() method to return.
     */

    abstract static class Interruptible implements Runnable {
        // The thread on which run() is invoked. This is set by ThreadManager.
        private Thread runThread;

        /**
         * Must cause run() method to return immediately, i.e, by closing socket
         * on which run() might be blocked or calling Thread.interrupt(). If
         * run() has not yet been called, this method must insure that
         * subsequent calls to run() will also return immediately. Must be
         * callable from any thread. Note: No guarantee that run() is actually
         * running when this method is called; it may be called before, during,
         * or after run() method. Care must be taken to insure it works properly
         * in all cases.
         */

        public abstract void interrupt();

        /**
         * Derived classes may use this method to obtain the thread on which the
         * run() method is executing; this is useful if the interrupt() method
         * needs to invoke thread.interrupt(). Note that this method will block
         * until that thread name is available.
         */
        protected Thread getRunThread() throws InterruptedException {
            synchronized (this) {
                while (runThread == null)
                    wait();
            }
            return runThread;
        }
    }

    /* ThreadManager schedules runnables of this type, not of Interruptible. */
    private class ManagedRunnable implements Runnable {
        private Interruptible target;

        ManagedRunnable(Interruptible target) {
            this.target = target;
        }

        public void run() {
            try {
                synchronized (target) {
                    target.runThread = Thread.currentThread();
                    target.notifyAll();
                }
                target.run();
            } finally {
                running.remove(this);
            }
        }

        public void stop() throws InterruptedException {
            target.interrupt();
            target.getRunThread().join();
        }
    }

    /** Creates and starts a new thread for the given Interruptible. */
    public void run(final Interruptible target, String name) {
        run(target, name, 0);
    }

    /**
     * Creates and starts a new thread for the given Interruptible. The thread
     * priority is adjusted by the relative value given by priorityBoost.
     */
    public void run(final Interruptible target, String name, int priorityBoost) {
        // We wrap the given Interruptible with a new one that sets the
        // current thread when run starts and removes this thread from
        // the manager when it stops.

        ManagedRunnable mr = new ManagedRunnable(target);

        // Hold a lock on isStopping when we add to running to be sure
        // stopAll() doesn't get a bad copy of the running list.

        synchronized (isStopping) {
            if (isStopping.booleanValue())
                throw new IllegalStateException("stopAll() has been called");
            running.add(mr);
        }
        Thread t = new Thread(mr, name);
        t.setPriority(t.getPriority() + priorityBoost);
        t.start();
    }

    /**
     * Stops all threads registered with this manager. Does not return until all
     * threads have exited.
     */
    public void stopAll() throws InterruptedException {
        // Set isStopping to block any subsequent calls to run() from
        // scheduling new things.

        synchronized (isStopping) {
            isStopping = new Boolean(true);
        }
        // Holding a lock on 'running' prevents threads from exiting, as well
        // as starting, so we can't call stop() while we have the lock. Make
        // a copy to avoid this.

        List toNotify = null;
        synchronized (running) {
            toNotify = new ArrayList(running);
        }
        Iterator i = toNotify.iterator();
        while (i.hasNext()) {
            ManagedRunnable mr = (ManagedRunnable) i.next();
            mr.stop();
        }
    }

    private List running = Collections.synchronizedList(new ArrayList());
    private Boolean isStopping = new Boolean(false);
}
