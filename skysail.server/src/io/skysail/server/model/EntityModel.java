//package io.skysail.server.model;
//
//import io.skysail.server.restlet.resources.SkysailServerResource;
//
//import java.util.*;
//
//import lombok.*;
//import de.twenty11.skysail.server.core.FormField;
//
//@ToString
//public class EntityModel {
//
//    @Getter
//    private List<FieldDescriptor> fieldDescriptors = new ArrayList<>();
//    
//    public EntityModel(List<FormField> formfields) {
//        formfields.stream().forEach(f -> {
//            fieldDescriptors.add(new FieldDescriptor(f));
//        });
//    }
//
//    public Map<String, Object> dataFromMap(Map<String, Object> props, SkysailServerResource<?> resource) {
//        Map<String,Object> result = new HashMap<>();
//        fieldDescriptors.stream().forEach(f -> {
//           result.putAll(f.dataFromMap(props, resource));  
//        });
//        return result;
//    }
//
//    public boolean isSubmitButtonNeeded() {
//        return !fieldDescriptors.stream().filter(f -> {return f.isSubmitField();}).findFirst().isPresent();
//    }
//    
//    
//
//}
