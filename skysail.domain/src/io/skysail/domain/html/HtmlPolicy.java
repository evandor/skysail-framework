package io.skysail.domain.html;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

/**
 * An HtmlPolicy defines which (html) content is allowed in a user-provided
 * string value (typically an entities' field with a {@link Field} annotation).
 *
 */
public enum HtmlPolicy {

    NO_HTML(
            Collections.<String> emptyList(), Collections.<AllowedAttribute> emptyList()
           ),
    TRIX_EDITOR(
            Arrays.asList(
                "a", "b", "p", "ul", "li", "i", "strong", "em", "br",
                "h1", "h2", "h3", "h4", "h5", "h6",
                "span", "div", "blockquote", "pre", "sup", "sub"),
            Arrays.asList(
                new AllowedAttribute("style").onElements("span", "div", "p"),
                new AllowedAttribute("href").onElements("a"))
           ),
    DEFAULT_HTML(
            Arrays.asList(
                "a", "b", "p", "ul", "li", "i", "strong", "em", "br",
                "h1", "h2", "h3", "h4", "h5", "h6",
                "span", "div", "blockquote", "pre", "sup", "sub"),
            Arrays.asList(
                new AllowedAttribute("style").onElements("span", "div", "p"),
                new AllowedAttribute("href").onElements("a"))
           );

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
