//package io.skysail.server.utils;
//
//import java.lang.reflect.Field;
//
//import io.skysail.domain.html.Relation;
//import lombok.extern.slf4j.Slf4j;
//
//@Slf4j
//public class AnnotationUtils {
//
//    public static Object removeRelationData(Object object) {
//        for (Field field : object.getClass().getDeclaredFields()) {
//            if (field.getDeclaredAnnotation(Relation.class) != null) {
//                try {
//                    field.setAccessible(true);
//                    field.set(object, null);
//                } catch (IllegalArgumentException | IllegalAccessException e) {
//                    log.error(e.getMessage(), e);
//                }
//            }
//        }
//        return object;
//    }
//
//}
