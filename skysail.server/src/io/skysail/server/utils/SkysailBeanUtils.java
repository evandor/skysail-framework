package io.skysail.server.utils;

import io.skysail.api.forms.InputType;
import io.skysail.server.forms.FormField;
import io.skysail.server.restlet.resources.SkysailServerResource;

import java.beans.PropertyDescriptor;
import java.lang.reflect.InvocationTargetException;
import java.util.*;

public class SkysailBeanUtils {

    private SkysailBeanUtilsBean beanUtilsBean;

    public SkysailBeanUtils(Object bean, Locale locale) {
        beanUtilsBean = new SkysailBeanUtilsBean(bean, locale);
    }

    public void populate(Object bean, Map<String, ? extends Object> properties) throws IllegalAccessException, InvocationTargetException {
        beanUtilsBean.populate(bean, properties);
    }

    public void copyProperties(Object dest, Object orig, SkysailServerResource<?> resource) throws IllegalAccessException, InvocationTargetException, NoSuchMethodException  {
        Map<String, FormField> formfields = FormfieldUtils.determineFormfields(resource);
        PropertyDescriptor[] origDescriptors = beanUtilsBean.getPropertyUtils().getPropertyDescriptors(orig);
        for (int i = 0; i < origDescriptors.length; i++) {
            String name = origDescriptors[i].getName();
            if ("class".equals(name) || ignore(formfields, name)) {
                continue;
            }
            if (beanUtilsBean.getPropertyUtils().isReadable(orig, name) &&
                    beanUtilsBean.getPropertyUtils().isWriteable(dest, name)) {
                try {
                    Object value =
                            beanUtilsBean.getPropertyUtils().getSimpleProperty(orig, name);
                    beanUtilsBean.copyProperty(dest, name, value);
                } catch (NoSuchMethodException e) {
                    // Should not happen
                }
            }
        }

    }

    private boolean ignore(Map<String, FormField> formfields, String name) {
        if ("id".equals(name)) {
            return false;
        }
        FormField formField = formfields.get(name);
        if (formField == null) {
            return true;
        }
        if (InputType.READONLY.name().toLowerCase().equals(formField.getInputType())) { // TODO why not use the enum directly?
            return true;
        }
        return false;
    }

}
