package de.twenty11.skysail.api.forms;

import org.apache.commons.lang.Validate;

/**
 * An html attribute (like "style") to be allowed on various elements (like "p", "span").
 * 
 * This is used for defining {@link HtmlPolicies} to prevent XSS attacks.
 * 
 */
public class AllowedAttribute {

    private String name;
    private String[] elements;

    public AllowedAttribute(String name) {
        Validate.notNull(name);
        this.name = name;
    }

    public AllowedAttribute onElements(String... elements) {
        this.elements = elements;
        return this;
    }

    public String getName() {
        return name;
    }

    public String[] getForElements() {
        return elements;
    }

}
