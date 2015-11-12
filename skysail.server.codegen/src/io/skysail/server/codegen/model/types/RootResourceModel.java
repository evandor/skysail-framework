package io.skysail.server.codegen.model.types;

import io.skysail.server.restlet.resources.ListServerResource;

public class RootResourceModel extends JavaModel {

	public RootResourceModel() {
		this.packageName = "pkgName";
		this.typeName = "RootResource";
		this.extendedModel = new SimpleJavaModel(ListServerResource.class, "String");
	}

}
