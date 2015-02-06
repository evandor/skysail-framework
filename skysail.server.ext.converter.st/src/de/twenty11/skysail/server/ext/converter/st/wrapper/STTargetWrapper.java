package de.twenty11.skysail.server.ext.converter.st.wrapper;

import org.restlet.representation.Variant;

import de.twenty11.skysail.server.app.SkysailApplication;

public class STTargetWrapper {

	private Variant target;

	public STTargetWrapper(Variant target) {
		this.target = target;
    }

	public boolean isTreeForm() {
		return SkysailApplication.SKYSAIL_TREE_FORM.getName().equals(target.getMediaType().getName());
	}
	
	@Override
	public String toString() {
	    return target.toString();
	}
}
