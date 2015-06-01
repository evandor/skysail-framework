//package io.skysail.server.converter.impl.factories;
//
//import io.skysail.server.converter.impl.FieldFactory;
//import io.skysail.server.restlet.resources.SkysailServerResource;
//import io.skysail.server.utils.ReflectionUtils;
//
//import java.lang.reflect.Field;
//import java.util.List;
//import java.util.stream.Collectors;
//
//import de.twenty11.skysail.server.core.FormField;
//
//@Deprecated // use class from skysail.server
//public class DefaultListFieldFactory extends FieldFactory {
//
//    private Object source;
//
//    public DefaultListFieldFactory(Object source) {
//        this.source = source;
//    }
//
//    @Override
//    public List<FormField> determineFrom(SkysailServerResource<?> resource) throws Exception {
//        Class<?> parameterType = resource.getParameterType();
//        Object entity = parameterType.newInstance();
//        return ReflectionUtils.getInheritedFields(resource.getParameterType()).stream().filter(f -> test(resource, f))
//                .sorted((f1, f2) -> sort(resource, f1, f2))
//                .map(f -> new FormField(f, resource, source, entity))//
//                .collect(Collectors.toList());
//    }
//
//    private int sort(SkysailServerResource<?> resource, Field f1, Field f2) {
//        List<String> fieldNames = resource.getFields();
//        return fieldNames.indexOf(f1.getName()) - fieldNames.indexOf(f2.getName());
//    }
//}
