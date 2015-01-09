package de.twenty11.skysail.api.responses;

import java.util.Map;

import org.restlet.Application;
import org.restlet.Context;

import aQute.bnd.annotation.ProviderType;

/**
 * Link abstraction to be used in Resources (see HATEOAS).
 *
 * Based on http://tools.ietf.org/search/rfc4287
 *
 */
@ProviderType
public class Link {

    private String href;

    private String title = "";

    public Link() {
        // needed for jackson
    }

    public Link(String href, String title) {
        this.href = href.trim();
        this.title = title;
    }

    /**
     * Constructor.
     * 
     * @param context
     * @param href
     * @param title
     */
    public Link(Context context, String href, String title) {
        this.href = href.trim();
        this.title = title;
        handleContext(context);
    }

    /**
     * 
     * Constructor.
     * 
     * @param restletApplication
     * @param relativeHref
     * @param title
     */
    public Link(Application restletApplication, String relativeHref, String title) {
        this.href = new StringBuilder("/").append(restletApplication.getName()).append("/").append(relativeHref.trim())
                .toString();
        this.title = title;

    }

    private void handleContext(Context context) {
        if (context == null) {
            return;
        }
        @SuppressWarnings("unchecked")
        Map<String, Object> parameterMap = (Map<String, Object>) context.getAttributes().get(
                "skysail_context_parameters");
        if (parameterMap.isEmpty()) {
            return;
        }
        if (href.contains("?")) {
            href += "&";
        } else {
            href += "?";
        }
        for (String paramName : parameterMap.keySet()) {
            href += paramName + "=" + parameterMap.get(paramName) + "&";
        }
        href = href.substring(0, href.length() - 1);

    }

    public String getHref() {
        return href;
    }

    public String getTitle() {
        return title;
    }

    @Override
    public String toString() {
        return new StringBuilder().append(title).append(" -> '").append(href).append("'").toString();
    }
}
