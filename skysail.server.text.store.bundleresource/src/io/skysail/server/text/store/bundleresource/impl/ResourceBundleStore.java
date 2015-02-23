package io.skysail.server.text.store.bundleresource.impl;

import io.skysail.api.text.TranslationStore;

import java.util.Collections;
import java.util.List;
import java.util.Locale;
import java.util.MissingResourceException;
import java.util.Optional;
import java.util.ResourceBundle;

import lombok.extern.slf4j.Slf4j;

import org.restlet.Request;
import org.restlet.util.Series;

import aQute.bnd.annotation.component.Component;
import de.twenty11.skysail.server.utils.HeadersUtils;

@Component(immediate = true, properties = { org.osgi.framework.Constants.SERVICE_RANKING + "=100" })
@Slf4j
// http://viralpatel.net/blogs/eclipse-resource-is-out-of-sync-with-the-filesystem/
public class ResourceBundleStore implements TranslationStore {

    /**
     * get the ResourceBundle translation for the given key using the default
     * locale.
     */
    @Override
    public Optional<String> get(String key) {
        return Optional.ofNullable(translate(key,
                ResourceBundle.getBundle("translations/messages", Locale.getDefault())));
    }

    /**
     * get the ResourceBundle translation for the given key and classloader
     * using the default locale.
     */
    @Override
    public Optional<String> get(String key, ClassLoader cl) {
        return Optional.ofNullable(translate(key,
                ResourceBundle.getBundle("translations/messages", Locale.getDefault(), cl)));
    }

    /**
     * get the ResourceBundle translation for the given key using the first
     * match for the accepted locales according to the request.
     */
    @Override
    public Optional<String> get(String key, ClassLoader cl, Request request) {
        Optional<String> translation = findAcceptedLanguages(request).stream().map(l -> {
            ResourceBundle dummy = getBundle(cl, l);
            // if (dummy == null) {
            // return (Optional<String>) Optional<String>.empty();
            // }
                return translate(key, dummy);
            }).filter(t -> {
            return t != null;
        }).findFirst();
        return translation;
    }

    private ResourceBundle getBundle(ClassLoader cl, String l) {
        try {
            return ResourceBundle.getBundle("translations/messages", new Locale(l), cl);
        } catch (MissingResourceException e) {
            return null;
        }
    }

    /**
     * get the ResourceBundle translation for the given key using the provided
     * locale.
     */
    @Override
    public Optional<String> get(String key, ClassLoader cl, Request request, Locale locale) {
        return findAcceptedLanguages(request).stream().filter(l -> {
            return translate(key, ResourceBundle.getBundle("translations/messages", locale, cl)) != null;
        }).findFirst();
    }

    private List<String> findAcceptedLanguages(Request request) {
        Series<?> headers = (Series<?>) request.getAttributes().get("org.restlet.http.headers");
        if (headers == null) {
            return Collections.emptyList();
        }
        String acceptLanguage = headers.getFirstValue("Accept-language");
        List<String> acceptedLanguages = HeadersUtils.parseAcceptedLanguages(acceptLanguage);
        return acceptedLanguages;
    }

    private String translate(String key, ResourceBundle resourceBundle) {
        try {
            return resourceBundle.getString(key);
        } catch (MissingResourceException mre) {
            log.debug(mre.getMessage());
        }
        return null;
    }
}
