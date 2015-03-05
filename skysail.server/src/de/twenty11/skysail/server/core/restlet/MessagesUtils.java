package de.twenty11.skysail.server.core.restlet;

import de.twenty11.skysail.server.core.FormField;

public class MessagesUtils {

    public static String getBaseKey(Class<? extends Object> entityClass, FormField f) {
        if (entityClass == null) {
            return "unnamedEntity";
        }
        if (entityClass.getName().contains("_$$_")) {
            entityClass = entityClass.getSuperclass();
        }
        return entityClass.getName() + "." + f.getName();
    }

    public static String getSimpleName(FormField f) {
        return f.getName();
    }

}
