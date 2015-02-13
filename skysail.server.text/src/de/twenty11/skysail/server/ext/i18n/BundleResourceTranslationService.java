package de.twenty11.skysail.server.ext.i18n;

import java.util.Collections;
import java.util.List;
import java.util.Locale;
import java.util.MissingResourceException;
import java.util.ResourceBundle;

import org.restlet.Request;
import org.restlet.util.Series;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import de.twenty11.skysail.api.services.TranslationService;
import de.twenty11.skysail.server.utils.HeadersUtils;

//@Component(immediate = true)
@Deprecated
// use new api.text service
public class BundleResourceTranslationService implements TranslationService {

    private static final Logger logger = LoggerFactory.getLogger(BundleResourceTranslationService.class);

    @Override
    public String translate(ClassLoader cl, Request request, String key, String defaultValue) {

        List<String> acceptedLanguages = findAcceptedLanguages(request);
        for (String language : acceptedLanguages) {
            try {

                Locale locale = new Locale(language);
                // http://viralpatel.net/blogs/eclipse-resource-is-out-of-sync-with-the-filesystem/
                ResourceBundle resourceBundle = ResourceBundle.getBundle("translations/messages", locale, cl);
                String translation = translate(key, resourceBundle);
                if (translation != null) {
                    return translation;
                }
            } catch (MissingResourceException mre) {
                logger.info(mre.getMessage());
            }
        }
        return defaultValue;
    }

    private String translate(String key, ResourceBundle resourceBundle) {
        String translation = null;
        try {
            translation = resourceBundle.getString(key);
        } catch (MissingResourceException mre) {
            logger.debug(mre.getMessage());
        }
        return translation;
    }

    private List<String> findAcceptedLanguages(Request request) {
        Series<?> headers = (Series<?>) request.getAttributes().get("org.restlet.http.headers");
        if (headers == null) {
            return Collections.emptyList();
        }
        String acceptLanguage = headers.getFirstValue("Accept-Language");
        List<String> acceptedLanguages = HeadersUtils.parseAcceptedLanguages(acceptLanguage);
        return acceptedLanguages;
    }

}
