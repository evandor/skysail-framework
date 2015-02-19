package io.skysail.api.text;

import org.restlet.Request;

/**
 * A Translation render service asks a TranslationStore for the translation
 * object for a given key and passes this to its render function to create the
 * translated value
 *
 */
public interface TranslationRenderService {

    /**
     * Get the best-fit translation (according to some internal algorithm) from
     * all translation stores using the provided classloader and request.
     * 
     * @param key
     * @param cl
     * @param request
     * @return
     */
    Translation getTranslation(String key, ClassLoader cl, Request request);

    /**
     * Get the translation for the given key using the provided classloader,
     * request and translation store.
     * 
     * @param key
     * @param cl
     * @param request
     * @param store
     * @return
     */
    Translation getTranslation(String key, ClassLoader cl, Request request, TranslationStore store);

    /**
     * Transform the provided translation using some specific renderer and the
     * provided substitutions.
     * 
     * @param translation
     * @param substitutions
     * @return
     */
    String render(Translation translation, Object... substitutions);
}
