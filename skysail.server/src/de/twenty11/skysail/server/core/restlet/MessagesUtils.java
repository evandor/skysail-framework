package de.twenty11.skysail.server.core.restlet;

import io.skysail.server.forms.FormField;

public class MessagesUtils {

    public static String getBaseKey(Class<? extends Object> entityClass, FormField f) {
        if (entityClass == null) {
            return "unnamedEntity";
        }
        if (entityClass.getName().contains("_$$_")) {
            entityClass = entityClass.getSuperclass();
        }
        return entityClass.getName() + "." + f.getId();
    }

    public static String getSimpleName(FormField f) {
        return f.getId();
    }

}
