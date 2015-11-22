package de.twenty11.skysail.server.mgt.jmx;

import io.skysail.api.domain.Identifiable;

import java.lang.management.MemoryUsage;

import lombok.*;

@Getter
public class MemorySnapshot implements Identifiable {

    private static final int FACTOR = 1024 * 1024; // MByte

    @Setter
    private String id;
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


}
