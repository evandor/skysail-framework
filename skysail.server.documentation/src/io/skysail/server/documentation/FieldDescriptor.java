package io.skysail.server.documentation;

import io.skysail.api.forms.Field;
import io.skysail.api.links.Link;
import io.skysail.server.app.SkysailApplication;
import io.skysail.server.forms.FormField;
import io.skysail.server.restlet.resources.SkysailServerResource;
import io.skysail.server.utils.LinkUtils;

import java.lang.annotation.Annotation;
import java.lang.reflect.Method;
import java.util.*;

import lombok.*;

@Getter
@ToString
public class FieldDescriptor {

    @Getter
    public class ListViewDescriptor {

        private String link;
        private Class<? extends Annotation> annotationType;
        private Map<String, String> colorize;
        private boolean hide;
        private String prefix;
        private int truncate;

        public ListViewDescriptor(FormField ff, SkysailApplication app) {
            annotationType = ff.getListViewAnnotation().annotationType();
            Class<? extends SkysailServerResource<?>> linkedResource = ff.getListViewAnnotation().link();
            if (linkedResource != null) {
                Link theLink = LinkUtils.fromResource(app, linkedResource);
                if (theLink != null) {
                    link = "<a href='" + theLink.getUri() + "'>{title}</a>";
                }
            }

            if (ff.getListViewAnnotation().colorize() != null) {
                if (ff.getType().isEnum()) {
                    Class<?> enumClass = (Class<?>) ff.getType();
                    Object[] enumValues = enumClass.getEnumConstants();
                    String colorizeMethod = ff.getListViewAnnotation().colorize();
                    colorize = new HashMap<>();
                    Arrays.stream(enumValues).forEach(
                            value -> {
                                try {
                                    Method getColorMethod = enumClass.getMethod(
                                            "get" + colorizeMethod.substring(0, 1).toUpperCase() + colorizeMethod.substring(1));
                                    String theColor = (String) getColorMethod.invoke(value);
                                    colorize.put(value.toString(), theColor);
                                } catch (Exception e) {
                                    e.printStackTrace();
                                }
                            });
                }
            }

            hide = ff.getListViewAnnotation().hide();
            prefix = ff.getListViewAnnotation().prefix();
            truncate = ff.getListViewAnnotation().truncate();
        }

    }

    @Field
    private Class<?> type;

    @Field
    private String inputType;

    private String classname;

    private ListViewDescriptor listViewDescriptor;

    public FieldDescriptor(FormField ff, SkysailApplication app) {
        this.classname = ff.getName();
        this.type = ff.getType();
        this.inputType = ff.getInputType();
        if (ff.getListViewAnnotation() != null) {
            listViewDescriptor = new ListViewDescriptor(ff, app);
        }
    }
}
