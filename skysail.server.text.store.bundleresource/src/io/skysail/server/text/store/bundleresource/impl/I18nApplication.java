package io.skysail.server.text.store.bundleresource.impl;

import io.skysail.api.text.Translation;

import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.MissingResourceException;
import java.util.Optional;
import java.util.ResourceBundle;

import lombok.extern.slf4j.Slf4j;

import org.apache.commons.configuration.ConfigurationException;
import org.apache.commons.configuration.PropertiesConfiguration;
import org.apache.commons.lang.StringUtils;
import org.osgi.framework.Bundle;
import org.osgi.framework.wiring.BundleWiring;

import aQute.bnd.annotation.component.Component;
import de.twenty11.skysail.server.app.ApplicationProvider;
import de.twenty11.skysail.server.app.SkysailApplication;
import de.twenty11.skysail.server.core.restlet.ApplicationContextId;
import de.twenty11.skysail.server.core.restlet.RouteBuilder;
import de.twenty11.skysail.server.services.MenuItem;
import de.twenty11.skysail.server.services.MenuItemProvider;

@Component(immediate = true)
@Slf4j
public class I18nApplication extends SkysailApplication implements ApplicationProvider, MenuItemProvider {

    private static final int MIN_MATCH_LENGTH = 20;

    public I18nApplication() {
        super("i18n");
        addToAppContext(ApplicationContextId.IMG, "/static/img/silk/book_open.png");
    }

    @Override
    protected void attach() {
        router.attach(new RouteBuilder("", I18nRootResource.class));
        router.attach(new RouteBuilder("/messages/", MessagesResource.class));
        router.attach(new RouteBuilder("/messages/{key}", MessageResource.class));
        router.attach(new RouteBuilder("/messages/{key}/", PutMessageResource.class));
    }

    @Override
    public List<MenuItem> getMenuEntries() {
        MenuItem appMenu = new MenuItem("I18N", "/i18n", this);
        appMenu.setCategory(MenuItem.Category.APPLICATION_MAIN_MENU);
        return Arrays.asList(appMenu);
    }

    public List<BundleMessages> getBundleMessages(Locale locale) {
        Bundle[] bundles = getBundleContext().getBundles();
        List<BundleMessages> result = new ArrayList<>();
        Arrays.stream(bundles).forEach(b -> {
            ClassLoader loader = b.adapt(BundleWiring.class).getClassLoader();
            handleResourceBundle(loader, b, locale, result);
        });
        return result;
    }

    private void handleResourceBundle(ClassLoader loader, Bundle b, Locale locale, List<BundleMessages> result) {
        try {
            ResourceBundle resourceBundle = ResourceBundle.getBundle("translations/messages", locale, loader);
            result.add(new BundleMessages(b, resourceBundle));
        } catch (MissingResourceException mre) {
            // ignore
        }
    }

    public Message getMessage(String msgKey) {
        List<BundleMessages> messages = getBundleMessages(new Locale("en"));
        Optional<BundleMessages> bundleMessage = messages.stream().filter(bm -> {
            return bm.getMessages().keySet().contains(msgKey);
        }).findFirst();
        if (bundleMessage.isPresent()) {
            return new Message(msgKey, bundleMessage.get().getMessages().get(msgKey));
        }
        return new Message(msgKey);
    }

    /**
     * tries to create or update a message in a properties file of the matching
     * bundle.
     * 
     * The property file has to exist.
     * 
     * @param message
     * @return If successful, this method will return the file path of the file
     *         which was altered, otherwise null.
     */
    public String setMessage(Message message) {
        List<BundleMessages> messages = getBundleMessages(new Locale("en"));
        Optional<BundleMessages> bundleMessage = messages.stream().filter(bm -> {
            return bm.getMessages().keySet().contains(message.getMsgKey());
        }).findFirst();
        if (bundleMessage.isPresent()) {
            return update(message, bundleMessage.get());
        } else {
            return create(message);
        }
    }

    private String create(Message entity) {
        List<BundleMessages> messages = getBundleMessages(new Locale("en"));
        int lastIndexOfUppercaseLetter = firstIndexOfUppercaseLetter(entity.getMsgKey());
        if (lastIndexOfUppercaseLetter < MIN_MATCH_LENGTH) {
            return null;
        }
        String match = findMatch(entity.getMsgKey());
        if (StringUtils.isEmpty(match)) {
            log.warn("could not create new translation, as there was not match for the key '{}'", entity.getMsgKey());
            return null;
        }
        for (BundleMessages bundleMessages : messages) {
            Map<String, String> msgs = bundleMessages.getMessages();
            Optional<String> found = msgs.keySet().stream().filter(key -> {
                return key.startsWith(match);
            }).findFirst();
            if (found.isPresent()) {
                return update(entity, bundleMessages);
            }
        }
        return null;
    }

    private String findMatch(String msgKey) {
        int firstIndexOfUppercaseLetter = firstIndexOfUppercaseLetter(msgKey);
        return msgKey.substring(0, firstIndexOfUppercaseLetter - 1);
    }

    private String update(Message entity, BundleMessages bundleMessages) {
        String propertyFileName = bundleMessages.getBaseBundleName();
        Bundle bundle = bundleMessages.getBundle();
        Path propertiesFile = Paths.get("..", bundle.getSymbolicName().replace(".core", ""), "resources",
                propertyFileName + ".properties");

        PropertiesConfiguration props;
        try {
            props = new PropertiesConfiguration(propertiesFile.toFile());
            props.setProperty(entity.getMsgKey(), escape(entity.getMsg()));
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

    // @Override
    public String persist(Translation translation) {
        return null;
    }

    public int firstIndexOfUppercaseLetter(String str) {
        for (int i = 0; i < str.length() - 1; i++) {
            if (Character.isUpperCase(str.charAt(i))) {
                return i;
            }
        }
        return -1;
    }

}
