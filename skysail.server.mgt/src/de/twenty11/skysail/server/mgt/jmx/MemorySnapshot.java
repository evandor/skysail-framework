package de.twenty11.skysail.server.mgt.jmx;

import java.lang.management.MemoryUsage;

public class MemorySnapshot {

    private static final int FACTOR = 1024 * 1024; // MByte
    private long now;
    private long init;
    private long max;
    private long used;
    private long committed;

    public MemorySnapshot(long now, MemoryUsage mu) {
        this.now = now;
        init = Math.round(mu.getInit() / FACTOR);
        max = Math.round(mu.getMax() / FACTOR);
        used = Math.round(mu.getUsed() / FACTOR);
        committed = Math.round(mu.getCommitted() / FACTOR);
    }

    public long getInit() {
        return init;
    }

    public long getMax() {
        return max;
    }

    public long getUsed() {
        return used;
    }

    public long getCommitted() {
        return committed;
    }

    public long getNow() {
        return now;
    }

}
