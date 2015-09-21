//package io.skysail.server.documentation;
//
//import io.skysail.api.responses.SkysailResponse;
//import io.skysail.server.forms.FormField;
//import io.skysail.server.restlet.resources.EntityServerResource;
//
//import java.util.*;
//import java.util.stream.Collectors;
//
//import de.twenty11.skysail.server.core.restlet.ResourceContextId;
//
//public class EntityResource extends EntityServerResource<Map<String, FieldDescriptor>> {
//
//    @Override
//    protected void doInit() {
//        super.doInit();
//        addToContext(ResourceContextId.LINK_TITLE, "show Entity");
//    }
//
//    @Override
//    public Map<String, FieldDescriptor> getEntity() {
//        String className = getAttribute("id");
//
//        try {
//            Map<String, FormField> description = getApplication().describe(className);
//            List<FieldDescriptor> descriptors = description.values().stream().map(ff -> new FieldDescriptor(ff, getApplication()))
//                    .collect(Collectors.toList());
//            Map<String, FieldDescriptor> result = new LinkedHashMap<>();
//            descriptors.stream().forEach(d -> {
//                result.put(d.getClassname(), d);
//            });
//            return result;
//        } catch (Exception e) {
//            e.printStackTrace();
//        }
//        return Collections.emptyMap();
//    }
//
//    @Override
//    public SkysailResponse<?> eraseEntity() {
//        return null;
//    }
//
//}
