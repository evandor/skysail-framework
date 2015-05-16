package io.skysail.server.utils;

import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.Map;
import java.util.Optional;

import lombok.extern.slf4j.Slf4j;

import org.apache.commons.beanutils.DynaBean;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.orientechnologies.orient.core.record.impl.ODocument;
import com.orientechnologies.orient.object.enhancement.OObjectProxyMethodHandler;

import de.twenty11.skysail.server.beans.DynamicEntity;
import de.twenty11.skysail.server.beans.EntityDynaProperty;

@Slf4j
public class OrientDbUtils {
    
    private static final ObjectMapper mapper = new ObjectMapper();
    
    public static Map<String,Object> toMap(Object entity) {
        Method[] methods = entity.getClass().getMethods();
        Optional<Method> getHandlerMethod = Arrays.stream(methods).filter(m -> m.getName().contains("getHandler"))
                .findFirst();
        if (getHandlerMethod.isPresent()) {
            try {
                OObjectProxyMethodHandler handler = (OObjectProxyMethodHandler) getHandlerMethod.get().invoke(entity);
                ODocument doc = handler.getDoc();
                return mapper.readValue(doc.toJSON(), new TypeReference<Map<String, Object>>() {
                });
            } catch (Exception e) {
                log.error(e.getMessage(), e);
            }
        }
        return null;
    }

    public static void updateFromDynamicEntity(DynamicEntity entity) {
        Method[] methods = entity.getClass().getMethods();
        DynaBean dynaBean = entity.getInstance();
        Optional<Method> getHandlerMethod = Arrays.stream(methods).filter(m -> m.getName().contains("getHandler"))
                .findFirst();
        if (getHandlerMethod.isPresent()) {
            try {
                OObjectProxyMethodHandler handler = (OObjectProxyMethodHandler) getHandlerMethod.get().invoke(entity);
                ODocument doc = handler.getDoc();
               
                for (EntityDynaProperty property : entity.getProperties()) {
                    Object object = dynaBean.get(property.getName());
                    if (object != null) {
                        doc.field(property.getName(), object);
                    }
                }
                
            } catch (Exception e) {
                log.error(e.getMessage(), e);
            }
        }
    }

}
