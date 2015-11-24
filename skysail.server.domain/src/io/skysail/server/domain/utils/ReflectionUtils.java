//package io.skysail.server.domain.utils;
//
//import java.lang.reflect.*;
//import java.util.*;
//
//public class ReflectionUtils {
//
//    public static List<java.lang.reflect.Field> getInheritedFields(Class<?> type) {
//        List<java.lang.reflect.Field> result = new ArrayList<java.lang.reflect.Field>();
//
//        Class<?> i = type;
//        while (i != null && i != Object.class) {
//            while (i != null && i != Object.class) {
//                for (java.lang.reflect.Field field : i.getDeclaredFields()) {
//                    if (!field.isSynthetic()) {
//                        result.add(field);
//                    }
//                }
//                i = i.getSuperclass();
//            }
//        }
//
//        return result;
//    }
//
//    public static List<Method> getInheritedMethods(Class<?> type) {
//        List<Method> result = new ArrayList<Method>();
//
//        Class<?> i = type;
//        while (i != null && i != Object.class) {
//            while (i != null && i != Object.class) {
//                for (Method method : i.getDeclaredMethods()) {
//                    if (!method.isSynthetic()) {
//                        result.add(method);
//                    }
//                }
//                i = i.getSuperclass();
//            }
//        }
//
//        return result;
//    }
//
//    public static Class<?> getParameterizedType(Class<?> cls) {
//        ParameterizedType parameterizedType = getParameterizedType1(cls);
//        Type firstActualTypeArgument = parameterizedType.getActualTypeArguments()[0];
//        if (firstActualTypeArgument.getTypeName().startsWith("java.util.Map")) {
//            return Map.class;
//        }
//        return (Class<?>) firstActualTypeArgument;
//    }
//
//    private static ParameterizedType getParameterizedType1(Class<?> cls) {
//        Type genericSuperclass = cls.getGenericSuperclass();
//        if (genericSuperclass instanceof ParameterizedType) {
//            return (ParameterizedType) genericSuperclass;
//        }
//        if (genericSuperclass == null) {
//            Type[] genericInterfaces = cls.getGenericInterfaces();
//            //return getParameterizedType1(genericInterfaces[0].getClass());
//            Optional<Type> pt = Arrays.stream(genericInterfaces).filter(i -> i instanceof ParameterizedType).findFirst();
//            if (pt.isPresent()) {
//                return (ParameterizedType)pt.get();
//            }
//        }
//        return getParameterizedType1(cls.getSuperclass());
//    }
//
//}
