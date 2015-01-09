package de.twenty11.skysail.api.forms;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

/**
 * An HtmlPolicy defines which (html) content is allowed in a user-provided
 * string value (typically an entities' field with a {@link Field} annotation).
 * 
 */
public enum HtmlPolicy {

    // @formatter:off
    NO_HTML(Collections.<String> emptyList(), Collections.<AllowedAttribute> emptyList()), DEFAULT_HTML(Arrays.asList(
            "b", "p", "ul", "li", "i", "strong", "em", "h1", "h2", "h3", "h4", "h5", "h6", "span", "div", "blockquote",
            "pre", "sup", "sub"), Arrays.asList(new AllowedAttribute("style").onElements("span", "div", "p")));
    // @formatter:on

    private List<String> allowedElements;
    private List<AllowedAttribute> allowedAttributes;

    HtmlPolicy(List<String> allowedElements, List<AllowedAttribute> allowedAttributes) {
        this.allowedElements = allowedElements;
        this.allowedAttributes = allowedAttributes;
    }

    public List<String> getAllowedElements() {
        return Collections.unmodifiableList(allowedElements);
    }

    public List<AllowedAttribute> getAllowedAttributes() {
        return Collections.unmodifiableList(allowedAttributes);
    }

}
