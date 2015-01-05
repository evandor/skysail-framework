package de.twenty11.skysail.api.features;

import java.util.List;

public interface SkysailFeatures {
	
	List<StateRepository> getStateRepositories();

	StateRepository getStateRepository(String name);

}
