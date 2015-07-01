package io.skysail.api.text;

/**
 * A Translation render service asks a TranslationStore for the translation
 * object for a given key and passes this to its render function to create the
 * translated value.
 *
 */
public interface TranslationRenderService {

    /**
     * Get the best-fit translation (according to some internal algorithm) from
     * all translation stores using the provided classloader and request.
     */
    // Translation getTranslation(String key, ClassLoader cl, Request request);

    /**
     * Get the translation for the given key using the provided classloader,
     * request and translation store.
     */
    // Translation getTranslation(String key, ClassLoader cl, Request request,
    // TranslationStore store);

    String render(String content, Object... substitutions);

    /**
     * Transform the provided translation using the implemented renderer and the
     * provided substitutions.
     */
    String render(Translation translation, Object... substitutions);

    boolean applicable(String unformattedTranslation);

    String adjustText(String unformatted);

    String addRendererInfo();

}
