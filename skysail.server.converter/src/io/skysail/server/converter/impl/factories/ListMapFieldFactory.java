//package io.skysail.server.converter.impl.factories;
//
//import io.skysail.server.converter.impl.FieldFactory;
//import io.skysail.server.restlet.resources.SkysailServerResource;
//
//import java.util.*;
//import java.util.stream.Collectors;
//
//import de.twenty11.skysail.server.core.FormField;
//
//@Deprecated // use class from skysail.server
//public class ListMapFieldFactory extends FieldFactory {
//
//    @Override
//    public List<FormField> determineFrom(SkysailServerResource<?> resource) {
//        List<FormField> fields = new ArrayList<>();
//        List<Map<String, Object>> currentEntity = (List<Map<String, Object>>) resource.getCurrentEntity();
//        if (currentEntity.size() > 0) {
//            fields = currentEntity.get(0).keySet().stream().map(key -> {
//                return new FormField(key, currentEntity.get(0).get(key));
//            }).collect(Collectors.toList());
//        }
//        return fields;
//    }
//
//}
