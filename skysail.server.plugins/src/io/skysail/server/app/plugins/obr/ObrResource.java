package io.skysail.server.app.plugins.obr;

import de.twenty11.skysail.api.forms.Field;

public class ObrResource {

	@Field
	private String searchFor;
	
	public ObrResource() {
    }

	public ObrResource(String searchFor) {
		this.searchFor = searchFor;
    }

	public String getSearchFor() {
	    return searchFor;
    }
	
	public void setSearchFor(String searchFor) {
	    this.searchFor = searchFor;
    }
	
}
