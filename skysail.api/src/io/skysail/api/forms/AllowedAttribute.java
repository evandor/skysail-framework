package io.skysail.api.forms;

import java.io.Serializable;

import lombok.*;

/**
 * An html attribute (like "style") to be allowed on various elements (like "p",
 * "span").
 *
 * <p>
 * This is used for defining HtmlPolicies to prevent XSS attacks.
 * </p>
 *
 */
@SuppressWarnings("serial")
public class AllowedAttribute implements Serializable {

    @Getter
    private String name;
    private String[] elements;

    public AllowedAttribute(@NonNull String name) {
        this.name = name;
    }

    public AllowedAttribute onElements(String... elements) {
        this.elements = elements;
        return this;
    }

    public String[] getForElements() {
        return elements;
    }

}