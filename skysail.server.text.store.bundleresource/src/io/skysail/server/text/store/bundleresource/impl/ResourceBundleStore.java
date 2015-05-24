package io.skysail.server.text.store.bundleresource.impl;

import io.skysail.api.text.TranslationStore;
import io.skysail.server.utils.HeadersUtils;

import java.nio.file.*;
import java.util.*;

import lombok.extern.slf4j.Slf4j;

import org.apache.commons.configuration.*;
import org.apache.commons.lang.StringUtils;
import org.osgi.framework.*;
import org.osgi.framework.wiring.BundleWiring;
import org.osgi.service.component.ComponentContext;
import org.restlet.Request;
import org.restlet.util.Series;

import aQute.bnd.annotation.component.*;

@Component(immediate = true, properties = { org.osgi.framework.Constants.SERVICE_RANKING + "=100" })
@Slf4j
// http://viralpatel.net/blogs/eclipse-resource-is-out-of-sync-with-the-filesystem/
public class ResourceBundleStore implements TranslationStore {
    
    private static final int MIN_MATCH_LENGTH = 20;
    private ComponentContext ctx;
    
    @Activate
    private void activate(ComponentContext ctx) {
        this.ctx = ctx;
    }

    @Deactivate
    private void deactivate(ComponentContext ctx) {
        this.ctx = null;
    }

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
            return translate(key, dummy);
        }).filter(t -> {
            return t != null;
        }).findFirst();
        
        if (!translation.isPresent()) {
            List<BundleMessages> messages = getBundleMessages(new Locale("en"));
            Optional<BundleMessages> bundleMessage = messages.stream().filter(bm -> {
                return bm.getMessages().keySet().contains(key);
            }).findFirst();
            if (bundleMessage.isPresent()) {
                return Optional.of(bundleMessage.get().getMessages().get(key));
            }
        }
        
        
        
        return translation;
    }

    private List<BundleMessages> getBundleMessages(Locale locale) {
        if (ctx == null) {
            return Collections.emptyList();
        }
        Bundle[] bundles = ctx.getBundleContext().getBundles();
        List<BundleMessages> result = new ArrayList<>();
        Arrays.stream(bundles).forEach(b -> {
            BundleWiring wiring = b.adapt(BundleWiring.class);
            if (wiring != null) {
                ClassLoader loader = wiring.getClassLoader();
                handleResourceBundle(loader, b, locale, result);
            }
        });
        return result;
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
        if (resourceBundle == null) {
            return null;
        }
        try {
            return resourceBundle.getString(key);
        } catch (MissingResourceException mre) {
            log.debug(mre.getMessage());
        }
        return null;
    }

    @Override
    public boolean persist(String key, String message, Locale locale, BundleContext bundleContext) {
        List<BundleMessages> messages = getBundleMessages(locale, bundleContext);
        Optional<BundleMessages> bundleMessage = messages.stream().filter(bm -> {
            return bm.getMessages().keySet().contains(key);
        }).findFirst();
        String updatePath;
        if (bundleMessage.isPresent()) {
            updatePath = update(key, message, bundleMessage.get());
        } else {
            updatePath = create(key, message, bundleContext);
        }
        return updatePath != null;
    }
    
    private List<BundleMessages> getBundleMessages(Locale locale, BundleContext bundleContext) {
        Bundle[] bundles = bundleContext.getBundles();
        List<BundleMessages> result = new ArrayList<>();
        Arrays.stream(bundles).forEach(b -> {
            ClassLoader loader = b.adapt(BundleWiring.class).getClassLoader();
            handleResourceBundle(loader, b, locale, result);
        });
        return result;
    }
    
    private String create(String key, String message, BundleContext bundleConetxt) {
        List<BundleMessages> messages = getBundleMessages(new Locale("en"), bundleConetxt);
        int lastIndexOfUppercaseLetter = firstIndexOfUppercaseLetter(key);
        if (lastIndexOfUppercaseLetter < MIN_MATCH_LENGTH) {
            return null;
        }
        String match = findMatch(key);
        if (StringUtils.isEmpty(match)) {
            log.warn("could not create new translation, as there was not match for the key '{}'", key);
            return null;
        }
        for (BundleMessages bundleMessages : messages) {
            Map<String, String> msgs = bundleMessages.getMessages();
            Optional<String> found = msgs.keySet().stream().filter(k -> {
                return k.startsWith(match);
            }).findFirst();
            if (found.isPresent()) {
                return update(key, message, bundleMessages);
            }
        }
        return null;
    }
    
    private String findMatch(String msgKey) {
        int firstIndexOfUppercaseLetter = firstIndexOfUppercaseLetter(msgKey);
        return msgKey.substring(0, firstIndexOfUppercaseLetter - 1);
    }

    private String update(String key, String message, BundleMessages bundleMessages) {
        String propertyFileName = bundleMessages.getBaseBundleName();
        Bundle bundle = bundleMessages.getBundle();
        Path propertiesFile = Paths.get("..", bundle.getSymbolicName().replace(".core", ""), "resources",
                propertyFileName + ".properties");

        PropertiesConfiguration props;
        try {
            props = new PropertiesConfiguration(propertiesFile.toFile());
            props.setProperty(key, message);
            props.save();
        } catch (ConfigurationException e) {
            log.error(e.getMessage(), e);
            return null;
        }
        return propertiesFile.toString();
    }

    private String escape(String msg) {
        return msg.replace("{", "'{'").replace("}", "'}'").replace(", ", ",&nbsp;");
    }
    
    private int firstIndexOfUppercaseLetter(String str) {
        for (int i = 0; i < str.length() - 1; i++) {
            if (Character.isUpperCase(str.charAt(i))) {
                return i;
            }
        }
        return -1;
    }
    
    private void handleResourceBundle(ClassLoader loader, Bundle b, Locale locale, List<BundleMessages> result) {
        try {
            ResourceBundle resourceBundle = ResourceBundle.getBundle("translations/messages", locale, loader);
            result.add(new BundleMessages(b, resourceBundle));
        } catch (MissingResourceException mre) {
            // ignore
        }
    }

    
}
