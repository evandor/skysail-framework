package io.skysail.server.utils;

import java.lang.reflect.InvocationTargetException;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.*;

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

    public void copyProperties(Object dest, Object orig) throws IllegalAccessException, InvocationTargetException  {
        beanUtilsBean.copyProperties(dest, orig);
    }
}
