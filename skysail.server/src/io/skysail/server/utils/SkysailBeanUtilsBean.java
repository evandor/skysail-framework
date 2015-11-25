package io.skysail.server.utils;

import io.skysail.api.domain.Identifiable;
import io.skysail.api.repos.DbRepository;

import java.beans.*;
import java.lang.reflect.*;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.*;
import java.util.stream.Collectors;

import lombok.extern.slf4j.Slf4j;

import org.apache.commons.beanutils.*;
import org.apache.commons.beanutils.expression.Resolver;
import org.apache.commons.lang3.StringUtils;

@Slf4j
public class SkysailBeanUtilsBean extends BeanUtilsBean {

    private static final String DATE_PATTERN = "yyyy-MM-dd";

    public void setProperty(Object bean, String name, Object value) throws IllegalAccessException,
            InvocationTargetException {

        // Resolve any nested expression to get the actual target bean
        Object target = bean;
        Resolver resolver = getPropertyUtils().getResolver();
        while (resolver.hasNested(name)) {
            try {
                target = getPropertyUtils().getProperty(target, resolver.next(name));
                if (target == null) { // the value of a nested property is null
                    return;
                }
                name = resolver.remove(name);
            } catch (NoSuchMethodException e) {
                return; // Skip this property setter
            }
        }

        // Declare local variables we will require
        String propName = resolver.getProperty(name); // Simple name of target
                                                      // property
        Class<?> type = null; // Java type of target property
        int index = resolver.getIndex(name); // Indexed subscript value (if any)
        String key = resolver.getKey(name); // Mapped key value (if any)

        // Calculate the property type
        if (target instanceof DynaBean) {
            throw new IllegalStateException("cannot handle DynaBeans");
        } else if (target instanceof Map) {
            type = Object.class;
        } else if (target != null && target.getClass().isArray() && index >= 0) {
            type = Array.get(target, index).getClass();
        } else {
            PropertyDescriptor descriptor = null;
            try {
                descriptor = getPropertyUtils().getPropertyDescriptor(target, name);
                if (descriptor == null) {
                    return; // Skip this property setter
                }
            } catch (NoSuchMethodException e) {
                return; // Skip this property setter
            }
            if (descriptor instanceof MappedPropertyDescriptor) {
                if (((MappedPropertyDescriptor) descriptor).getMappedWriteMethod() == null) {
                    if (log.isDebugEnabled()) {
                        log.debug("Skipping read-only property");
                    }
                    return; // Read-only, skip this property setter
                }
                type = ((MappedPropertyDescriptor) descriptor).getMappedPropertyType();
            } else if (index >= 0 && descriptor instanceof IndexedPropertyDescriptor) {
                if (((IndexedPropertyDescriptor) descriptor).getIndexedWriteMethod() == null) {
                    if (log.isDebugEnabled()) {
                        log.debug("Skipping read-only property");
                    }
                    return; // Read-only, skip this property setter
                }
                type = ((IndexedPropertyDescriptor) descriptor).getIndexedPropertyType();
            } else if (key != null) {
                if (descriptor.getReadMethod() == null) {
                    if (log.isDebugEnabled()) {
                        log.debug("Skipping read-only property");
                    }
                    return; // Read-only, skip this property setter
                }
                type = (value == null) ? Object.class : value.getClass();
            } else {
                if (descriptor.getWriteMethod() == null) {
                    if (log.isDebugEnabled()) {
                        log.debug("Skipping read-only property");
                    }
                    return; // Read-only, skip this property setter
                }
                type = descriptor.getPropertyType();
            }
        }

        // Convert the specified value to the required type
        Object newValue = null;
        if (type.isArray() && (index < 0)) { // Scalar value into array
            if (value == null) {
                String[] values = new String[1];
                values[0] = null;
                newValue = getConvertUtils().convert(values, type);
            } else if (value instanceof String) {
                newValue = getConvertUtils().convert(value, type);
            } else if (value instanceof String[]) {
                newValue = getConvertUtils().convert((String[]) value, type);
            } else {
                newValue = convert(value, type);
            }
        } else if (type.isArray()) { // Indexed value into array
            if (value instanceof String || value == null) {
                newValue = getConvertUtils().convert((String) value, type.getComponentType());
            } else if (value instanceof String[]) {
                newValue = getConvertUtils().convert(((String[]) value)[0], type.getComponentType());
            } else {
                newValue = convert(value, type.getComponentType());
            }
        } else { // Value into scalar
            if (value instanceof String && Collection.class.isAssignableFrom(type)) {
                try {
                    newValue = new ArrayList<Identifiable>();
                    Class<?> parameterizedType = getParameterizedType(bean, name);
                    createAndSetNewIdentifiables(Arrays.asList((String)value), newValue, parameterizedType);
                } catch (NoSuchFieldException | SecurityException e) {
                    log.error(e.getMessage(), e);
                }
            } else if (value instanceof Collection && Collection.class.isAssignableFrom(type)) {
                try {
                    newValue = new ArrayList<Identifiable>();
                    Class<?> parameterizedType = getParameterizedType(bean, name);
                    if (parameterizedType != null) {
                        createAndSetNewIdentifiables((Collection<?>) value, newValue, parameterizedType);
                    }
                } catch (NoSuchFieldException | SecurityException e) {
                    log.error(e.getMessage(), e);
                }
            } else if (value instanceof String) {
                newValue = getConvertUtils().convert((String) value, type);
            } else if (value instanceof String[]) {
                newValue = getConvertUtils().convert(((String[]) value)[0], type);
            } else {
                newValue = convert(value, type);
            }
        }

        // Invoke the setter method
        try {
            getPropertyUtils().setProperty(target, name, newValue);
        } catch (NoSuchMethodException e) {
            throw new InvocationTargetException(e, "Cannot set " + propName);
        }

    }

    private void createAndSetNewIdentifiables(Collection<?> values, Object newValue, Class<?> parameterizedType) {
        if (Identifiable.class.isAssignableFrom(parameterizedType)) {
            List<String> ids = values.stream().map(v -> v.toString()).collect(Collectors.toList());
            for (String id : ids) {
                createAndSetNewIdentifiable(id, newValue, parameterizedType);
            }
        }
    }

    private Class<?> getParameterizedType(Object bean, String name) throws NoSuchFieldException {
        Field declaredField = bean.getClass().getDeclaredField(name);
        io.skysail.api.forms.Field fieldAnnotation = declaredField.getAnnotation(io.skysail.api.forms.Field.class);
        if (fieldAnnotation == null) {
            return null;
        }
        Class<? extends DbRepository> repository = fieldAnnotation.repository();
        return ReflectionUtils.getParameterizedType(repository);
    }

    @SuppressWarnings("unchecked")
    private void createAndSetNewIdentifiable(Object value, Object newValue, Class<?> parameterizedType) {
        Identifiable newInstance;
        try {
            newInstance = (Identifiable) parameterizedType.newInstance();
            newInstance.setId((String) value);
            ((List<Identifiable>) newValue).add(newInstance);
        } catch (Exception e) {
            log.error(e.getMessage(), e);
        }
    }

    public SkysailBeanUtilsBean(Object bean, Locale locale) {
        super(new ConvertUtilsBean() {
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

}
