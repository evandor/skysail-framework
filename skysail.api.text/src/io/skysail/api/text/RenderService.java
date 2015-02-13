package io.skysail.api.text;

import org.restlet.Request;

/**
 * A render service asks a Store for the translation object for a given key and
 * passes this to its render function to
 *
 */
public interface RenderService {

    Translation getTranslation(String key, ClassLoader cl, Request request, Store store);

    String render(Translation translation, Object... substitutions);
}
