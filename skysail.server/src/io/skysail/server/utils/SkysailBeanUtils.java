package io.skysail.server.utils;

import io.skysail.api.forms.InputType;
import io.skysail.server.forms.FormField;
import io.skysail.server.restlet.resources.SkysailServerResource;

import java.beans.PropertyDescriptor;
import java.lang.reflect.InvocationTargetException;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.*;
import java.util.Map.Entry;

import lombok.extern.slf4j.Slf4j;

import org.apache.commons.beanutils.*;
import org.apache.commons.lang3.StringUtils;

@Slf4j
public class SkysailBeanUtils {

    private static final String DATE_PATTERN = "yyyy-MM-dd";
    private BeanUtilsBean beanUtilsBean;

    public SkysailBeanUtils(Locale locale) {
        beanUtilsBean = new BeanUtilsBean(new ConvertUtilsBean() {
            @SuppressWarnings("unchecked")
            @Override
            public Object convert(String value, @SuppressWarnings("rawtypes") Class clazz) {
                if (clazz.isEnum()) {
                    return Enum.valueOf(clazz, value);
                } else if (clazz.equals(LocalDate.class)) {
                    if (StringUtils.isEmpty(value)) {
                        return null;
                    }
                    DateTimeFormatter sdf = DateTimeFormatter.ofPattern(DATE_PATTERN, locale);
                    try {
                        return LocalDate.parse(value, sdf);
                    } catch (Exception e) {
                        log.info("could not parse date '{}' with pattern {}", value, DATE_PATTERN);
                    }
                    return null;
                } else if (clazz.equals(Date.class)) {
                    if (StringUtils.isEmpty(value)) {
                        return null;
                    }
                    SimpleDateFormat sdf = new SimpleDateFormat(DATE_PATTERN, locale);
                    try {
                        return sdf.parse(value);
                    } catch (Exception e) {
                        log.info("could not parse date '{}' with pattern {}", value, DATE_PATTERN);
                    }
                    return null;
                } else {
                    return super.convert(value, clazz);
                }
            }

        });
    }

    public void populate(Object bean, Map<String, ? extends Object> properties) throws IllegalAccessException, InvocationTargetException {
        beanUtilsBean.populate(bean, properties);
    }

    public void copyProperties(Object dest, Object orig, SkysailServerResource<?> resource) throws IllegalAccessException, InvocationTargetException, NoSuchMethodException  {
        Map<String, String> described = beanUtilsBean.describe(dest);
        Map<String, FormField> formfields = FormfieldUtils.determineFormfields(resource);

        PropertyDescriptor[] origDescriptors = beanUtilsBean.getPropertyUtils().getPropertyDescriptors(orig);
        for (int i = 0; i < origDescriptors.length; i++) {
            String name = origDescriptors[i].getName();
            if ("class".equals(name)) {
                continue; // No point in trying to set an object's class
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

        formfields.entrySet().stream().filter(entry -> test(entry)).forEach(entry -> {
          try {
            beanUtilsBean.copyProperty(dest, entry.getKey(), described.get(entry.getKey()));
        } catch (Exception e) {
            throw new RuntimeException("Problem copying property "  + entry.getKey(), e);
        }
        });

        beanUtilsBean.copyProperties(dest, orig);
    }

    private boolean test(Entry<String, FormField> entry) {
        FormField formfield = entry.getValue();
        if (InputType.READONLY.name().toLowerCase().equals(formfield.getInputType())) { // TODO
            return false;
        }
        return true;
     }
}
