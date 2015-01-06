package de.twenty11.skysail.server.utils;

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;

public class ReflectionUtils {

    public static List<java.lang.reflect.Field> getInheritedFields(Class<?> type) {
        List<java.lang.reflect.Field> result = new ArrayList<java.lang.reflect.Field>();

        Class<?> i = type;
        while (i != null && i != Object.class) {
            while (i != null && i != Object.class) {
                for (java.lang.reflect.Field field : i.getDeclaredFields()) {
                    if (!field.isSynthetic()) {
                        result.add(field);
                    }
                }
                i = i.getSuperclass();
            }
        }

        return result;
    }

    public static List<Method> getInheritedMethods(Class<?> type) {
        List<Method> result = new ArrayList<Method>();

        Class<?> i = type;
        while (i != null && i != Object.class) {
            while (i != null && i != Object.class) {
                for (Method method : i.getDeclaredMethods()) {
                    if (!method.isSynthetic()) {
                        result.add(method);
                    }
                }
                i = i.getSuperclass();
            }
        }

        return result;
    }
}
