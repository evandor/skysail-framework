package de.twenty11.skysail.server.resources;

import java.util.Collections;
import java.util.List;

import de.twenty11.skysail.server.core.restlet.ListServerResource;

public class AboutResource extends ListServerResource<String> {

	@Override
    public List<String> getData() {
	    return Collections.emptyList();
    }
	

}
