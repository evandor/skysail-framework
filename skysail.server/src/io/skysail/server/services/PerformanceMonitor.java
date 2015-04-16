package io.skysail.server.services;

public interface PerformanceMonitor {

    PerformanceTimer start(String identifier);
    
}
