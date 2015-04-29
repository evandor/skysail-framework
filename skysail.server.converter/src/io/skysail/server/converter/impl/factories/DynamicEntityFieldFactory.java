package io.skysail.server.converter.impl.factories;

import io.skysail.server.converter.impl.FieldFactory;
import io.skysail.server.restlet.resources.SkysailServerResource;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

import org.apache.commons.beanutils.DynaProperty;

import de.twenty11.skysail.server.beans.DynamicEntity;
import de.twenty11.skysail.server.core.FormField;

public class DynamicEntityFieldFactory extends FieldFactory {

    private DynamicEntity entity;

    public DynamicEntityFieldFactory(DynamicEntity entity) {
        this.entity = entity;
    }

    @Override
    public List<FormField> determineFrom(SkysailServerResource<?> resource) throws Exception {
        DynaProperty[] dynaProperties = ((DynamicEntity) entity).getInstance().getDynaClass()
                .getDynaProperties();
        return Arrays.stream(dynaProperties)
                .filter(d -> {
                    return !d.getType().equals(List.class);
                })
                .map(d -> {
            return new FormField((DynamicEntity) entity, d, resource);
        }).collect(Collectors.toList());
    }

}
