package io.skysail.server.ext.apt.model.types;

import javax.lang.model.element.Element;

import de.twenty11.skysail.server.core.restlet.ListServerResource;

public class RootResourceModel extends JavaModel {

	public RootResourceModel(Element skysailApplicationElement) {
		this.packageName = skysailApplicationElement.getEnclosingElement().toString();
		this.typeName = "RootResource";
		this.extendedModel = new SimpleJavaModel(ListServerResource.class, "String");
	}

}
