package de.twenty11.skysail.server.core;

import io.skysail.api.forms.*;
import io.skysail.api.responses.*;
import io.skysail.server.forms.ListView;
import io.skysail.server.restlet.resources.SkysailServerResource;

import java.lang.reflect.*;
import java.lang.reflect.Field;
import java.util.*;

import javax.validation.constraints.*;

import lombok.*;
import lombok.extern.slf4j.Slf4j;

import org.restlet.resource.Resource;

import de.twenty11.skysail.server.core.restlet.MessagesUtils;
import de.twenty11.skysail.server.um.domain.SkysailUser;

/**
 * A FormField instance encapsulates (meta) information which can be used to display a
 * single field in a (web) form, table or entity representation.
 * 
 * <p>
 * A FormField is constructed from a java.lang.reflect.Field, together with
 * a SkysailServerResource.
 * </p>
 * 
 * <p>This class will not try to determine the actual value of the field, this is
 * left to further processing.</p>
 *
 */
@Slf4j
@ToString(of = { "name", "type", "entity" })
public class FormField {

    /** the fields name or identifier. */
    @Getter
    private final String name;

//    /**
//     * the value to be passed to the GUI renderer.
//     * 
//     * <p>
//     * the value is not set from outside, but derived from the passed resource.
//     * </p>
//     */
//    @Getter
//    private String value;

    /** text, textarea, radio, checkbox etc... */
    private final InputType inputType;

    @Getter
    private Class<?> type;

    @Getter
    private Object entity;

    @Getter
    private Class<?> cls;

    private Reference referenceAnnotation;
    private io.skysail.api.forms.Field formFieldAnnotation;
    private SkysailServerResource<?> resource;
    private List<Option> selectionOptions;

    @Getter
    private ListView listViewAnnotation;

    private Submit submitAnnotation;
    private NotNull notNullAnnotation;
    private Size sizeAnnotation;

    @Getter
    private String violationMessage;

    public FormField(Field field, SkysailServerResource<?> resource) {
        name = field.getName();
        type = field.getType();
        inputType = getFromFieldAnnotation(field);
        setAnnotations(field);
        entity = resource.getCurrentEntity();
        this.resource = resource;
    }
    
    /**
     * @param field
     *            from java reflection
     * @param resource
     *            the resource which is about to being represented
     * @param source
     */
    public FormField(Field field, SkysailServerResource<?> resource, List<?> source) {
        this(field, resource);
        scan(field);
    }

    public FormField(Field field, SkysailServerResource<?> resource, ConstraintViolationsResponse<?> source) {
        this(field, resource);
        Set<ConstraintViolationDetails> violations = ((ConstraintViolationsResponse<?>) source).getViolations();
        Optional<String> validationMessage = violations.stream()
                .filter(v -> v.getPropertyPath().equals(field.getName())).map(v -> v.getMessage()).findFirst();
        violationMessage = validationMessage.orElse(null);
        scan(field);
    }
    
    public FormField(Field field, SkysailServerResource<?> resource, FormResponse<?> source) {
        this(field, resource);
        scan(field);
    }

    private void setAnnotations(Field field) {
        referenceAnnotation = field.getAnnotation(Reference.class);
        formFieldAnnotation = field.getAnnotation(io.skysail.api.forms.Field.class);
        listViewAnnotation = field.getAnnotation(ListView.class);
        submitAnnotation = field.getAnnotation(Submit.class);
        notNullAnnotation = field.getAnnotation(NotNull.class);
        sizeAnnotation = field.getAnnotation(Size.class);
    }

    private void scan(Field field) {
        if (entity == null) {
            return;
        }

//        Method[] methods = entity.getClass().getMethods();
//        String method = "get" + field.getName().substring(0, 1).toUpperCase() + field.getName().substring(1);
//        Arrays.stream(methods).filter(m -> m.getName().equals(method)).findFirst().ifPresent(m -> {
//            try {
//                Object invocationResult = m.invoke(entity, new Object[] {});
//                if (invocationResult == null) {
//                    value = "";
//                } else if (invocationResult instanceof String) {
//                    value = (String) invocationResult;
//                } else if (invocationResult instanceof Date) {
//                    this.type = Date.class;
//                    SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
//                    value = sdf.format(invocationResult);
//                } else {
//                    value = invocationResult.toString();
//                }
//            } catch (Exception e) {
//                log.error(e.getMessage(), e);
//            }
//        });
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

    public String getHref() {
        return null;
    }

    public boolean isDateType() {
        return checkTypeFor(Date.class);
    }

    public boolean isRangeType() {
        return isOfInputType(InputType.RANGE);
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

    public boolean isSubmitField() {
        return submitAnnotation != null;
    }

    public boolean isCheckbox() {
        if (formFieldAnnotation != null) {
            return InputType.CHECKBOX.equals(formFieldAnnotation.type());
        }
        return false;
    }

    public boolean isDate() {
        if (formFieldAnnotation != null) {
            return InputType.DATE.equals(formFieldAnnotation.type());
        }
        return false;
    }

    public boolean isRange() {
        if (formFieldAnnotation != null) {
            return InputType.RANGE.equals(formFieldAnnotation.type());
        }
        return false;
    }

    public int getRangeMin() {
        return 0;
    }

    public int getRangeMax() {
        return 100;
    }

    public boolean isLink() {
        if (listViewAnnotation != null) {
            return !listViewAnnotation.link().toString().equals("class io.skysail.server.forms.ListView$DEFAULT");
        }
        return false;
    }

    public List<Option> getSelectionProviderOptions() {
        if (!isSelectionProvider()) {
            throw new IllegalAccessError("not a selection provider");
        }
        if (selectionOptions != null) {
            return selectionOptions;
        }
        List<Option> options = new ArrayList<>();

        Class<? extends SelectionProvider> selectionProvider = null;
        if (formFieldAnnotation != null) {
            selectionProvider = formFieldAnnotation.selectionProvider();
        }

        if (referenceAnnotation != null) {
            selectionProvider = referenceAnnotation.selectionProvider();
        }
        if (selectionProvider == null) {
            return Collections.emptyList();
        }
        SelectionProvider selection;
        try {
            Method method = selectionProvider.getMethod("getInstance");
            selection = (SelectionProvider) method.invoke(selectionProvider, new Object[] {});

            method = selectionProvider.getMethod("setResource", Resource.class);
            method.invoke(selection, resource);
            selection.getSelections().entrySet().stream().forEach(entry -> {
               // options.add(new Option(entry, value));
            });
            selectionOptions = options;
            return options;
        } catch (Exception e) {
            log.error(e.getMessage(), e);
        }
        return Collections.emptyList();
    }

    public boolean isMandatory() {
        if (notNullAnnotation != null) {
            return true;
        }
        if (sizeAnnotation != null) {
            int min = sizeAnnotation.min();
            if (min > 0) {
                return true;
            }
        }
        return false;
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

//    private void handleMap(Field field, Map<String, Object> entityMap) {
//        Object val = entityMap.get(field.getName());
//        if (val == null) {
//            value = "---";
//        } else if (val instanceof String) {
//            value = (String) val;
//        } else if (val instanceof Date) {
//            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
//            value = (String) sdf.format(value);
//        } else {
//            value = val.toString();
//        }
//        type = field.getType();
//    }
}
