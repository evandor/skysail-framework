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
//import lombok.NonNull;
//import de.twenty11.skysail.server.core.FormField;
//
//@Deprecated // use class from skysail.server
//public class DefaultEntityFieldFactory extends FieldFactory {
//
//    private Object source;
//
//    public DefaultEntityFieldFactory(@NonNull Object source) {
//        this.source = source;
//    }
//
//    @Override
//    public List<FormField> determineFrom(SkysailServerResource<?> resource) throws Exception {
//        Class<?> cls = source.getClass();
//        List<Field> inheritedFields = ReflectionUtils.getInheritedFields(cls);
//        System.out.println(inheritedFields);
//        return ReflectionUtils.getInheritedFields(cls).stream()
//                .filter(f -> test(resource, f))
//                .map(f -> new FormField(f, resource, source))
//                .collect(Collectors.toList());
//       // return Collections.emptyList();
//    }
//
//}
