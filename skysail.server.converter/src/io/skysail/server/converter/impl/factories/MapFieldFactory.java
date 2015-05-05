//package io.skysail.server.converter.impl.factories;
//
//import io.skysail.server.converter.impl.FieldFactory;
//import io.skysail.server.restlet.resources.SkysailServerResource;
//
//import java.util.ArrayList;
//import java.util.List;
//import java.util.Map;
//import java.util.stream.Collectors;
//
//import de.twenty11.skysail.server.core.FormField;
//
//public class MapFieldFactory extends FieldFactory {
//
//    private Map<String, Object> entityMap;
//
//    public MapFieldFactory(Map<String, Object> entityMap) {
//        this.entityMap = entityMap;
//    }
//
//    @Override
//    public List<FormField> determineFrom(SkysailServerResource<?> resource) {
//        List<FormField> fields = new ArrayList<>();
//        //Map<String, Object> currentEntity = (Map<String, Object>) resource.getCurrentEntity();
//        // if (currentEntity.get(0) instanceof DynamicEntity) {
//        // DynamicEntity dynEntity = (DynamicEntity) currentEntity.get(0);
//        // fields = createFieldsForDynamicEntity(resource, dynEntity);
//        // } else {
//        fields = entityMap.keySet().stream().map(key -> {
//            return new FormField(key, entityMap.get(key));
//        }).collect(Collectors.toList());
//        // }
//        return fields;
//    }
//
//}
