package io.skysail.server.ext.apt.model.types;

import io.skysail.server.menus.MenuItemProvider;

import java.util.*;

import de.twenty11.skysail.server.app.ApplicationProvider;
import de.twenty11.skysail.server.ext.apt.annotations.SkysailApplication;

public class ApplicationModel2 extends JavaModel {

	public ApplicationModel2() {
		this.packageName = "pkgName";//skysailApplicationElement.getEnclosingElement().toString();
		this.typeName = "typeName";//skysailApplicationElement.getSimpleName().toString() + "Gen";
		this.extendedModel = new SimpleJavaModel(SkysailApplication.class);
		this.implementedModels = new HashSet<JavaModel>(Arrays.asList(//
		        new SimpleJavaModel(ApplicationProvider.class), //
		        new SimpleJavaModel(MenuItemProvider.class)));
	}
}
