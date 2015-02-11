package io.skysail.server.ext.apt.model.types;

import java.util.Arrays;
import java.util.HashSet;

import javax.lang.model.element.Element;

import de.twenty11.skysail.server.app.ApplicationProvider;
import de.twenty11.skysail.server.app.SkysailApplication;
import de.twenty11.skysail.server.services.MenuItemProvider;

public class ApplicationModel extends JavaModel {

	public ApplicationModel(Element skysailApplicationElement) {
		this.packageName = skysailApplicationElement.getEnclosingElement().toString();
		this.typeName = skysailApplicationElement.getSimpleName().toString() + "Gen";
		this.extendedModel = new SimpleJavaModel(SkysailApplication.class);
		this.implementedModels = new HashSet<JavaModel>(Arrays.asList(//
		        new SimpleJavaModel(ApplicationProvider.class), //
		        new SimpleJavaModel(MenuItemProvider.class)));
	}
}
