package de.twenty11.skysail.server.metrics;

public interface MetricsService {

    Counter getCounter(String identifier);

	Meter getMeter(String identifier);

	<T> void register(String identifier, Gauge<T> gauge);


}
