package de.twenty11.skysail.server.core;

import io.skysail.api.forms.IgnoreSelectionProvider;
import io.skysail.api.forms.InputType;
import io.skysail.api.forms.Reference;
import io.skysail.api.forms.SelectionProvider;
import io.skysail.api.responses.ConstraintViolationDetails;
import io.skysail.api.responses.ConstraintViolationsResponse;
import io.skysail.api.responses.SkysailResponse;
import io.skysail.server.restlet.resources.SkysailServerResource;
import io.skysail.server.utils.OrientDbUtils;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.Collections;
import java.util.Date;
import java.util.Map;
import java.util.Optional;
import java.util.Set;

import lombok.Getter;
import lombok.extern.slf4j.Slf4j;

import org.apache.commons.beanutils.DynaBean;
import org.apache.commons.beanutils.DynaProperty;
import org.restlet.resource.Resource;

import de.twenty11.skysail.server.beans.DynamicEntity;
import de.twenty11.skysail.server.beans.EntityDynaProperty;
import de.twenty11.skysail.server.core.restlet.MessagesUtils;
import de.twenty11.skysail.server.um.domain.SkysailUser;

/**
 * A FormField instance encapsulates information which can be used to display a
 * single field in a (web) form.
 *
 */
@Slf4j
public class FormField {

    /** the fields name or identifier. */
    @Getter
    private String name;

    /** the value to be passed to the GUI renderer. */
    @Getter
    private String value;

    private Object source;
    private Class<?> type;
    private Object entity;
    private Class<?> cls;

    private InputType inputType;
    private Reference referenceAnnotation;
    private io.skysail.api.forms.Field formFieldAnnotation;

    private SkysailServerResource<?> resource;

    private Map<String, String> selectionOptions;

    public FormField(Field field, SkysailServerResource<?> resource, Object source) {
        this.resource = resource;
        this.name = field.getName();
        inputType = getFromFieldAnnotation(field);
        referenceAnnotation = field.getAnnotation(Reference.class);
        formFieldAnnotation = field.getAnnotation(io.skysail.api.forms.Field.class);
        this.source = source;
        
        Map<String, Object> entityMap = OrientDbUtils.toMap(source);
        if (entityMap != null) {
            Object val = entityMap.get(field.getName());
            if (val == null) {
                value = "---";
            } else if (val instanceof String) {
                value = (String) val;
            } else {
                value = val.toString();
            }
            type = field.getType();

        } else {
//            Method[] methods = entity.getClass().getMethods();
//            String method = "get" + field.getName().substring(0, 1).toUpperCase()
//                    + field.getName().substring(1);
//            Arrays.stream(methods).filter(m -> m.getName().equals(method)).findFirst().ifPresent(m -> {
//                try {
//                    Object invocationResult = m.invoke(entity, new Object[] {});
//                    if (invocationResult == null) {
//                        value = "";
//                    } else if (invocationResult instanceof String) {
//                        value = (String) invocationResult;
//                    } else if (invocationResult instanceof Date) {
//                        this.type = Date.class;
//                        value = invocationResult.toString();
//                    } else {
//                        value = invocationResult.toString();
//                    }
//                } catch (Exception e) {
//                    log.error(e.getMessage(), e);
//                }
//            });
        }

    }
   

    public FormField(Field field, SkysailServerResource<?> resource, Object source, Object entity) {
        this.resource = resource;
        this.name = field.getName();
        inputType = getFromFieldAnnotation(field);
        referenceAnnotation = field.getAnnotation(Reference.class);
        formFieldAnnotation = field.getAnnotation(io.skysail.api.forms.Field.class);
        this.source = source;
        this.entity = entity;

        Map<String, Object> entityMap = OrientDbUtils.toMap(entity);
        if (entityMap != null) {
            Object val = entityMap.get(field.getName());
            if (val == null) {
                value = "---";
            } else if (val instanceof String) {
                value = (String) val;
            } else {
                value = val.toString();
            }
            type = field.getType();

        } else {
            Method[] methods = entity.getClass().getMethods();
            String method = "get" + field.getName().substring(0, 1).toUpperCase()
                    + field.getName().substring(1);
            Arrays.stream(methods).filter(m -> m.getName().equals(method)).findFirst().ifPresent(m -> {
                try {
                    Object invocationResult = m.invoke(entity, new Object[] {});
                    if (invocationResult == null) {
                        value = "";
                    } else if (invocationResult instanceof String) {
                        value = (String) invocationResult;
                    } else if (invocationResult instanceof Date) {
                        this.type = Date.class;
                        value = invocationResult.toString();
                    } else {
                        value = invocationResult.toString();
                    }
                } catch (Exception e) {
                    log.error(e.getMessage(), e);
                }
            });
        }
    }

    public FormField(DynamicEntity dynamicBean, DynaProperty d, SkysailServerResource<?> resource) {
        this.name = d.getName();
        DynaBean instance = dynamicBean.getInstance();
        Object valueOrNull = instance.get(name);
        this.value = valueOrNull == null ? null : valueOrNull.toString();
        this.inputType = InputType.TEXT;
        if (d instanceof EntityDynaProperty) {
            this.inputType = ((EntityDynaProperty) d).getInputType();
        }
        this.cls = dynamicBean.getClass();
    }

    public FormField(String key, Object object) {
        this.name = key;
        this.value = object != null ? object.toString() : "";
        this.inputType = InputType.TEXT;
    }

    public Object getEntity() {
        if (source instanceof SkysailResponse) {
            return ((SkysailResponse<?>) source).getEntity();
        }
        return entity;// source.getEntity();
    }

    public Class<?> getCls() {
        return cls;
    }

    public String getInputType() {
        return inputType.name().toLowerCase();
    }

    public String getMessageKey() {
        return MessagesUtils.getBaseKey(cls != null ? cls : getEntity().getClass(), this) + ".desc";
    }

    public String getNameKey() {
        if (cls == null && getEntity() == null) {
            return name;
        }
        return MessagesUtils.getBaseKey(cls != null ? cls : getEntity().getClass(), this);
    }

    public String getPlaceholderKey() {
        return MessagesUtils.getBaseKey(cls != null ? cls : getEntity().getClass(), this) + ".placeholder";
    }

    public String getTitleKey() {
        return MessagesUtils.getBaseKey(cls != null ? cls : getEntity().getClass(), this) + ".title";
    }

    public boolean isDateType() {
        return checkTypeFor(Date.class);
    }

    public boolean isSkysailUserType() {
        return checkTypeFor(SkysailUser.class);
    }

    public boolean isTagsInputType() {
        return isOfInputType(InputType.TAGS);
    }

    public boolean isReadonlyInputType() {
        return isOfInputType(InputType.READONLY);
    }

    public boolean isMarkdownEditorInputType() {
        return isOfInputType(InputType.MARKDOWN_EDITOR);
    }

    public boolean isTextareaInputType() {
        return isOfInputType(InputType.TEXTAREA);
    }

    public boolean isSelectionProvider() {

        if (formFieldAnnotation != null) {
            Class<? extends SelectionProvider> selectionProvider = formFieldAnnotation.selectionProvider();
            if (!(selectionProvider.equals(IgnoreSelectionProvider.class))) {
                return true;
            }
        }
        if (referenceAnnotation == null) {
            return false;
        }
        if (!(IgnoreSelectionProvider.class.equals(referenceAnnotation.selectionProvider()))) {
            return true;
        }
        return false;
    }

    public boolean isCheckbox() {
        if (formFieldAnnotation != null) {
            return InputType.CHECKBOX.equals(formFieldAnnotation.type());
        }
        return false;
    }

    public Map<String, String> getSelectionProviderOptions() {
        if (!isSelectionProvider()) {
            throw new IllegalAccessError("not a selection provider");
        }
        if (selectionOptions != null) {
            return selectionOptions;
        }
        Class<? extends SelectionProvider> selectionProvider = null;
        if (formFieldAnnotation != null) {
            selectionProvider = formFieldAnnotation.selectionProvider();
        }

        if (referenceAnnotation != null) {
            selectionProvider = referenceAnnotation.selectionProvider();
        }
        if (selectionProvider == null) {
            return Collections.emptyMap();
        }
        SelectionProvider selection;
        try {
            Method method = selectionProvider.getMethod("getInstance");
            selection = (SelectionProvider) method.invoke(selectionProvider, new Object[] {});

            method = selectionProvider.getMethod("setResource", Resource.class);
            method.invoke(selection, resource);
            selectionOptions = selection.getSelections();
            return selectionOptions;
        } catch (Exception e) {
            log.error(e.getMessage(), e);
        }
        return Collections.emptyMap();
    }

    public String getViolationMessage() {
        if (!(source instanceof ConstraintViolationsResponse)) {
            return null;
        }
        String fieldName = getName();
        Set<ConstraintViolationDetails> violations = ((ConstraintViolationsResponse<?>) source).getViolations();
        Optional<String> validationMessage = violations.stream().filter(v -> v.getPropertyPath().equals(fieldName))
                .map(v -> v.getMessage()).findFirst();
        return validationMessage.orElse(null);
    }

    private boolean isOfInputType(InputType inputType) {
        return this.inputType.equals(inputType);
    }

    private boolean checkTypeFor(Class<?> cls) {
        return this.type != null && this.type.equals(cls);
    }

    private InputType getFromFieldAnnotation(Field fieldAnnotation) {
        io.skysail.api.forms.Field annotation = fieldAnnotation.getAnnotation(io.skysail.api.forms.Field.class);
        return annotation != null ? annotation.type() : null;
    }

    @Override
    public String toString() {
        return new StringBuilder("FormField: [").append(name).append("=").append(value).append("], isReadonly: ")
                .append(isReadonlyInputType()).append(", isTextareaInputType: ").append(isTextareaInputType())
                .append(", isMarkdownEditorInputType: ").append(isMarkdownEditorInputType())
                .append(", isSelectionProvider: ").append(isSelectionProvider()).append(", isTagsInputType: ")
                .append(isTagsInputType()).toString();
    }

}
