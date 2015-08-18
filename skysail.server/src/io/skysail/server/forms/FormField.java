package io.skysail.server.forms;

import io.skysail.api.forms.*;
import io.skysail.api.responses.*;
import io.skysail.server.forms.helper.CellRendererHelper;
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
 * A FormField instance encapsulates (meta) information which can be used to
 * display a single field in a (web) form, or a column in a table
 * representation.
 *
 * <p>
 * A FormField is constructed from a java.lang.reflect.Field, together with a
 * SkysailServerResource; only {@link Field}s and {@link Reference}s should be taken into account.
 *
 * </p>
 *
 * <p>
 * This class will not try to determine the actual value of the field, this is
 * left to further processing.
 * </p>
 *
 */
// TODO make Field annotation nested
@Slf4j
@ToString(of = { "name", "type", "inputType" })
@Getter
public class FormField {

    /** the fields name or identifier, e.g. "title" */
    private final String name;

    /** the fields (java) type, e.g. java.lang.String */
    private final Class<?> type;

    /** text, textarea, radio, checkbox etc... */
    private final InputType inputType; // optional for a reference

    private Reference referenceAnnotation;
    private io.skysail.api.forms.Field formFieldAnnotation;
    private Submit submitAnnotation;
    private NotNull notNullAnnotation;
    private Size sizeAnnotation;
    private ListView listViewAnnotation;

    private SkysailServerResource<?> resource;
    private List<Option> selectionOptions;

    @Getter
    private String violationMessage;

    public FormField(Field field, SkysailServerResource<?> resource) {
        name = field.getName();
        type = field.getType();
        inputType = getFromFieldAnnotation(field);
        setAnnotations(field);
        this.resource = resource;
    }

    public FormField(Field field, SkysailServerResource<?> resource, ConstraintViolationsResponse<?> source) {
        this(field, resource);
        Set<ConstraintViolationDetails> violations = ((ConstraintViolationsResponse<?>) source).getViolations();
        Optional<String> validationMessage = violations.stream()
                .filter(v -> v.getPropertyPath().equals(field.getName())).map(v -> v.getMessage()).findFirst();
        violationMessage = validationMessage.orElse(null);
    }

    private void setAnnotations(Field field) {
        referenceAnnotation = field.getAnnotation(Reference.class);
        formFieldAnnotation = field.getAnnotation(io.skysail.api.forms.Field.class);
        listViewAnnotation = field.getAnnotation(ListView.class);
        submitAnnotation = field.getAnnotation(Submit.class);
        notNullAnnotation = field.getAnnotation(NotNull.class);
        sizeAnnotation = field.getAnnotation(Size.class);
    }

    public String getInputType() {
        return inputType != null ? inputType.name().toLowerCase() : "";
    }

    public String getMessageKey() {
        return MessagesUtils.getBaseKey(resource.getCurrentEntity().getClass(), this) + ".desc";
    }

    public String getNameKey() {
        Object entity = resource.getCurrentEntity();
        if (entity == null) {
            return name;
        }
        if (entity instanceof List && ((List<?>)entity).size() > 0) {
            return MessagesUtils.getBaseKey(((List<?>)entity).get(0).getClass(), this);
        }
        return MessagesUtils.getBaseKey(entity.getClass(), this);
    }

    public String getPlaceholderKey() {
        return MessagesUtils.getBaseKey(resource.getCurrentEntity().getClass(), this) + ".placeholder";
    }

    public String getTitleKey() {
        return MessagesUtils.getBaseKey(resource.getCurrentEntity().getClass(), this) + ".title";
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
            return InputType.CHECKBOX.equals(formFieldAnnotation.inputType());
        }
        return false;
    }

    public boolean isDate() {
        if (formFieldAnnotation != null) {
            return InputType.DATE.equals(formFieldAnnotation.inputType());
        }
        return false;
    }

    public boolean isRange() {
        if (formFieldAnnotation != null) {
            return InputType.RANGE.equals(formFieldAnnotation.inputType());
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
                options.add(new Option(entry, ""));
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

    public String process(SkysailResponse<?> response, Map<String, Object> dataRow, String columnName, Object id) {
        return new CellRendererHelper(this, response).render(dataRow.get(columnName), id);
    }

    private boolean isOfInputType(InputType inputType) {
        return this.inputType.equals(inputType);
    }

    private boolean checkTypeFor(Class<?> cls) {
        return this.type != null && this.type.equals(cls);
    }

    private InputType getFromFieldAnnotation(Field fieldAnnotation) {
        io.skysail.api.forms.Field annotation = fieldAnnotation.getAnnotation(io.skysail.api.forms.Field.class);
        return annotation != null ? annotation.inputType() : null;
    }


}
