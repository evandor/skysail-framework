package de.twenty11.skysail.api.forms;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * annotate entities' fields or methods with this annotation in order to let
 * skysail render it in its generic forms (and to define other things like
 * encryption and HtmlPolicies).
 *
 */
@Target({ ElementType.FIELD, ElementType.METHOD })
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface Field {

    /**
     * selection provider.
     * 
     * @return a selection provider for choices
     */
    Class<? extends SelectionProvider> selectionProvider() default IgnoreSelectionProvider.class;

    /**
     * @return which tags to apply this to
     */
    String[] tags() default { "all" };

    // http://www.w3.org/TR/REC-html40/interact/forms.html#h-17.4
    /**
     * @return field type
     */
    InputType type() default InputType.TEXT;

    /**
     * @return field name
     */
    String name() default "";

    /**
     * Define the poliy for HTML strings
     * 
     * @return the policy
     */
    HtmlPolicy htmlPolicy() default HtmlPolicy.NO_HTML;

    /**
     * enrypt the field using the passphrase provided by a parameter with this
     * name.
     *
     * @return passphrase
     */
    String encryptWith() default "";

    /**
     * Defines the way this entity attribute should be displayed in a list view.
     * 
     * @return show or hide
     */
    ListView[] listView() default ListView.SHOW;
}
