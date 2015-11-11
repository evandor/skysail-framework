package io.skysail.server.codegen.model.types;

import io.skysail.server.restlet.resources.ListServerResource;

import javax.lang.model.element.Element;

public class RootResourceModel extends JavaModel {

	public RootResourceModel(Element skysailApplicationElement) {
		this.packageName = skysailApplicationElement.getEnclosingElement().toString();
		this.typeName = "RootResource";
		this.extendedModel = new SimpleJavaModel(ListServerResource.class, "String");
	}

}
