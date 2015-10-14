package io.skysail.server.ext.apt.model.types;

import io.skysail.server.restlet.resources.ListServerResource;

import javax.lang.model.element.Element;

public class RootResourceModel2 extends JavaModel {

	public RootResourceModel2() {
		this.packageName = "pkgName";//skysailApplicationElement.getEnclosingElement().toString();
		this.typeName = "RootResource";
		this.extendedModel = new SimpleJavaModel(ListServerResource.class, "String");
	}

}
