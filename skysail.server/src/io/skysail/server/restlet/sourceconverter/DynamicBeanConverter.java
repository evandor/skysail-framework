//package io.skysail.server.restlet.sourceconverter;
//
//import io.skysail.server.restlet.resources.SkysailServerResource;
//
//import java.lang.reflect.Field;
//import java.util.ArrayList;
//import java.util.Arrays;
//import java.util.HashMap;
//import java.util.List;
//import java.util.Map;
//
//import org.apache.commons.beanutils.DynaBean;
//import org.apache.commons.beanutils.DynaProperty;
//
//import aQute.bnd.annotation.component.Component;
//import de.twenty11.skysail.server.app.AbstractSourceConverter;
//import de.twenty11.skysail.server.beans.DynamicEntity;
//
//@Component
//public class DynamicBeanConverter extends AbstractSourceConverter implements SourceConverter {
//
//    @Override
//    public boolean isCompatible() {
//        return (getSource() instanceof List && ((List) getSource()).size() != 0 && ((List) getSource()).get(0) instanceof DynamicEntity);
//    }
//
//    @Override
//    public Object convert(SkysailServerResource<?> resource, List<Field> fields) {
//        List<Map<String, Object>> result = new ArrayList<>();
//        for (Object object : (List<?>) getSource()) {
//            DynamicEntity dynamicEntity = (DynamicEntity) object;
//            Map<String, Object> map = new HashMap<>();
//
//            DynaBean instance = dynamicEntity.getInstance();
//            DynaProperty[] dynaProperties = instance.getDynaClass().getDynaProperties();
//            Arrays.stream(dynaProperties).forEach(p -> {
//                map.put(p.getName(), instance.get(p.getName()));
//            });
//            result.add(map);
//        }
//
//     
//        return result;
//    }
//}
