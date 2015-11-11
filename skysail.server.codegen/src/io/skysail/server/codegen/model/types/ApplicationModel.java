package io.skysail.server.codegen.model.types;

import io.skysail.server.codegen.SkysailApplication;
import io.skysail.server.ext.apt.model.types.*;
import io.skysail.server.menus.MenuItemProvider;

import java.util.*;

import javax.lang.model.element.Element;

import de.twenty11.skysail.server.app.ApplicationProvider;

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
