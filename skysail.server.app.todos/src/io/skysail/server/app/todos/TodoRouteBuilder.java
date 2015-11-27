package io.skysail.server.app.todos;

import io.skysail.server.restlet.resources.SkysailServerResource;

import org.restlet.Restlet;

import de.twenty11.skysail.server.core.restlet.RouteBuilder;

public class TodoRouteBuilder extends RouteBuilder {

    public TodoRouteBuilder(TodoApplication todoApplication, String pathTemplate, Class<? extends SkysailServerResource<?>> targetClass) {
        super(pathTemplate, newUserFilter(targetClass, todoApplication));
        setTargetClass(targetClass);
    }

    private static Restlet newUserFilter(Class<? extends SkysailServerResource<?>> targetClass, TodoApplication todoApplication) {
        return new NewUserFilter(targetClass,todoApplication);
    }

}
