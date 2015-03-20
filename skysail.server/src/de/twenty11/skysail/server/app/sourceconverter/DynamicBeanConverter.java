package de.twenty11.skysail.server.app.sourceconverter;

import io.skysail.api.responses.FormResponse;

import java.lang.reflect.Field;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.beanutils.DynaProperty;
import org.restlet.data.MediaType;

import aQute.bnd.annotation.component.Component;
import de.twenty11.skysail.server.app.AbstractSourceConverter;
import de.twenty11.skysail.server.beans.DynamicBean;
import de.twenty11.skysail.server.core.restlet.SkysailServerResource;

@Component
public class DynamicBeanConverter extends AbstractSourceConverter implements SourceConverter {

    @Override
    public boolean isCompatible() {
        if (!(getSource() instanceof FormResponse)) {
            return false;
        }
        return ((FormResponse<?>) getSource()).getEntity() instanceof DynamicBean
                && getTarget().getMediaType().equals(MediaType.TEXT_HTML);
    }

    @Override
    public Object convert(SkysailServerResource<?> resource, List<Field> fields) {
        FormResponse<?> formResponse = (FormResponse<?>) getSource();
        DynamicBean bean = (DynamicBean) formResponse.getEntity();

        Map<String, Object> result = new HashMap<>();

        DynaProperty[] dynaProperties = bean.getInstance().getDynaClass().getDynaProperties();
        Arrays.stream(dynaProperties).forEach(p -> {
            result.put(p.getName(), bean.getInstance().get(p.getName()));
        });

        // return new FormResponse<Map>(result, formResponse.getId(),
        // formResponse.getTarget(),
        // formResponse.getRedirectBackTo());
        return getSource();
    }
}
