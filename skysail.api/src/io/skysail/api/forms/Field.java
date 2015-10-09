package io.skysail.api.forms;

import io.skysail.api.forms.impl.NoRepository;
import io.skysail.api.repos.DbRepository;

import java.lang.annotation.*;

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
     */
    Class<? extends SelectionProvider> selectionProvider() default IgnoreSelectionProvider.class;


    Class<? extends DbRepository> repository() default NoRepository.class;

    /**
     * http://www.w3.org/TR/REC-html40/interact/forms.html#h-17.4
     */
    InputType inputType() default InputType.TEXT;

    /**
     * Define the poliy for HTML strings.
     *
     */
    HtmlPolicy htmlPolicy() default HtmlPolicy.NO_HTML;

    /**
     * enrypt the field using the passphrase provided by a parameter with this
     * name.
     *
     */
    String encryptWith() default "";

}
