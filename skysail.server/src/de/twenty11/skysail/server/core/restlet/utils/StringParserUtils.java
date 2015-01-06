package de.twenty11.skysail.server.core.restlet.utils;

import java.util.concurrent.ConcurrentMap;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.restlet.resource.Resource;

public class StringParserUtils {

    public static Pattern placeholderPattern = Pattern.compile("\\{([^\\}]*)\\}");

    public static String substitutePlaceholders(String uri, Resource entityResource) {
        if (uri == null) {
            return null;
        }
        Matcher matcher = placeholderPattern.matcher(uri);
        ConcurrentMap<String, Object> attributes = entityResource.getRequest().getAttributes();
        while (matcher.find()) {
            if (attributes == null) {
                continue;
            }
            String group = matcher.group();
            String replacement = (String) attributes.get(group.replace("}", "").replace("{", ""));
            uri = uri.replace(group, replacement != null ? replacement : group);
        }
        return uri;
    }

}
