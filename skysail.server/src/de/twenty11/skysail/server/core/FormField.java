package de.twenty11.skysail.server.core;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import java.util.Set;

import lombok.extern.slf4j.Slf4j;

import org.codehaus.jettison.json.JSONException;
import org.codehaus.jettison.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.orientechnologies.orient.core.record.impl.ODocument;
import com.orientechnologies.orient.object.enhancement.OObjectProxyMethodHandler;

import de.twenty11.skysail.api.forms.IgnoreSelectionProvider;
import de.twenty11.skysail.api.forms.InputType;
import de.twenty11.skysail.api.forms.SelectionProvider;
import de.twenty11.skysail.api.responses.ConstraintViolationDetails;
import de.twenty11.skysail.api.responses.ConstraintViolationsResponse;
import de.twenty11.skysail.api.responses.SkysailResponse;
import de.twenty11.skysail.server.core.restlet.MessagesUtils;
import de.twenty11.skysail.server.services.UserManager;
import de.twenty11.skysail.server.um.domain.SkysailUser;

@Slf4j
public class FormField {

    private static final Logger logger = LoggerFactory.getLogger(FormField.class);

    private final ObjectMapper mapper = new ObjectMapper();

    private Field fieldAnnotation;
    private Object source;
    private String value;

    // maybe pass this in the constructor...
    private Class<?> type;

    private Object entity;

    private Class<?> cls;

    private JSONObject json;

    @Deprecated
    public FormField(Field fieldAnnotation, UserManager userManager, Object source, Object entity) {
        this.fieldAnnotation = fieldAnnotation;
        this.source = source;
        this.entity = entity;

        Method[] methods = entity.getClass().getMethods();
        Optional<Method> getHandlerMethod = Arrays.stream(methods).filter(m -> m.getName().contains("getHandler"))
                .findFirst();
        if (getHandlerMethod.isPresent()) {
            try {
                HashMap<String, Object> objectMap = new HashMap<String, Object>();
                OObjectProxyMethodHandler handler = (OObjectProxyMethodHandler) getHandlerMethod.get().invoke(entity);
                ODocument doc = handler.getDoc();
                objectMap = mapper.readValue(doc.toJSON(), new TypeReference<Map<String, Object>>() {
                });
                value = (String) objectMap.get(fieldAnnotation.getName());
                type = fieldAnnotation.getType();
                if (type.equals(SkysailUser.class) && userManager != null && value != null) {
                    value = userManager.findById(value).getUsername();
                }
            } catch (Exception e) {
                logger.error(e.getMessage(), e);
            }
        } else {
            String method = "get" + fieldAnnotation.getName().substring(0, 1).toUpperCase()
                    + fieldAnnotation.getName().substring(1);
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
                    logger.error(e.getMessage(), e);
                }
            });
        }
    }

    public FormField(Field fieldAnnotation, UserManager userManager, Object source, JSONObject json, Class<?> cls) {
        this.fieldAnnotation = fieldAnnotation;
        this.source = source;
        this.json = json;
        this.cls = cls;

        try {
            value = json.getString(fieldAnnotation.getName());
        } catch (JSONException e) {
            log.error(e.getMessage(), e);
            value = "";
        }

    }

    public Field getFieldAnnotation() {
        return fieldAnnotation;
    }

    public Object getEntity() {
        if (source instanceof SkysailResponse) {
            return ((SkysailResponse<?>) source).getEntity();
        }
        return entity;// source.getEntity();
    }

    public String getName() {
        return fieldAnnotation.getName();
    }

    public String getValue() {
        return value;
    }

    public Class<?> getCls() {
        return cls;
    }

    public String getInputType() {
        return fieldAnnotation.getAnnotation(de.twenty11.skysail.api.forms.Field.class).type().name().toLowerCase();
    }

    public String getMessageKey() {
        return MessagesUtils.getBaseKey(cls != null ? cls : getEntity().getClass(), this) + ".desc";
    }

    public String getNameKey() {
        return MessagesUtils.getBaseKey(cls != null ? cls : getEntity().getClass(), this);
    }

    public String getPlaceholderKey() {
        return MessagesUtils.getBaseKey(cls != null ? cls : getEntity().getClass(), this) + ".placeholder";
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
        return InputType.TEXTAREA.equals(fieldAnnotation.getAnnotation(de.twenty11.skysail.api.forms.Field.class)
                .type());
    }

    public boolean isSelectionProvider() {
        de.twenty11.skysail.api.forms.Field annotation = fieldAnnotation
                .getAnnotation(de.twenty11.skysail.api.forms.Field.class);
        Class<? extends SelectionProvider> selectionProvider = annotation.selectionProvider();
        if (selectionProvider.equals(IgnoreSelectionProvider.class)) {
            return false;
        }
        return true;
    }

    public Map<String, String> getSelectionProviderOptions() {
        if (!isSelectionProvider()) {
            throw new IllegalAccessError("not a selection provider");
        }
        de.twenty11.skysail.api.forms.Field annotation = fieldAnnotation
                .getAnnotation(de.twenty11.skysail.api.forms.Field.class);
        Class<? extends SelectionProvider> selectionProvider = annotation.selectionProvider();

        SelectionProvider selection;
        try {
            Method method = selectionProvider.getMethod("getInstance");
            selection = (SelectionProvider) method.invoke(selectionProvider, new Object[] {});
            return selection.getSelections();
        } catch (Exception e) {
            logger.error(e.getMessage(), e);
        }
        return Collections.emptyMap();
    }

    public String getViolationMessage() {
        if (!(source instanceof ConstraintViolationsResponse)) {
            return null;
        }
        String fieldName = getFieldAnnotation().getName();
        Set<ConstraintViolationDetails> violations = ((ConstraintViolationsResponse<?>) source).getViolations();
        Optional<String> validationMessage = violations.stream().filter(v -> v.getPropertyPath().equals(fieldName))
                .map(v -> v.getMessage()).findFirst();
        return validationMessage.orElse(null);
    }

    private boolean isOfInputType(InputType inputType) {
        return inputType.equals(fieldAnnotation.getAnnotation(de.twenty11.skysail.api.forms.Field.class).type());
    }

    private boolean checkTypeFor(Class<?> cls) {
        return this.type != null && this.type.equals(cls);
    }

    @Override
    public String toString() {
        return new StringBuilder("FormField: [").append(value).append("], isReadonly: ").append(isReadonlyInputType())
                .append(", isTextareaInputType: ").append(isTextareaInputType())
                .append(", isMarkdownEditorInputType: ").append(isMarkdownEditorInputType())
                .append(", isSelectionProvider: ").append(isSelectionProvider()).append(", isTagsInputType: ")
                .append(isTagsInputType()).toString();
    }

}
