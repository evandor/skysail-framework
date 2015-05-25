//package io.skysail.server.converter.impl.factories;
//
//import io.skysail.server.converter.impl.FieldFactory;
//import io.skysail.server.restlet.resources.SkysailServerResource;
//import io.skysail.server.utils.OrientDbUtils;
//
//import java.util.Arrays;
//import java.util.List;
//import java.util.Map;
//import java.util.stream.Collectors;
//
//import org.apache.commons.beanutils.DynaProperty;
//
//import de.twenty11.skysail.server.beans.DynamicEntity;
//import de.twenty11.skysail.server.core.FormField;
//
//public class DynamicEntityFieldFactory extends FieldFactory {
//
//    private DynamicEntity entity;
//
//    public DynamicEntityFieldFactory(DynamicEntity entity) {
//        this.entity = entity;
//    }
//
//    @Override
//    public List<FormField> determineFrom(SkysailServerResource<?> resource) throws Exception {
//        DynaProperty[] dynaProperties = ((DynamicEntity) entity).getInstance().getDynaClass().getDynaProperties();
//        Map<String, Object> entityMap = OrientDbUtils.toMap(entity);
//        return Arrays.stream(dynaProperties).filter(d -> {
//            return !d.getType().equals(List.class);
//        }).map(d -> {
//            if (entityMap != null) {
//                Object value = entityMap.get(d.getName());
//                entity.getInstance().set(d.getName(), value);
//            }
//            return new FormField(entity, d, resource);
//        }).collect(Collectors.toList());
//    }
//
//}
