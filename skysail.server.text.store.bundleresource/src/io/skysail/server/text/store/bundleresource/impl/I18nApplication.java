package io.skysail.server.text.store.bundleresource.impl;

import io.skysail.api.text.Translation;
import io.skysail.api.text.TranslationStore;
import io.skysail.server.app.SkysailApplication;
import io.skysail.server.text.TranslationStoreHolder;
import io.skysail.server.utils.TranslationUtils;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.MissingResourceException;
import java.util.Optional;
import java.util.ResourceBundle;

import org.osgi.framework.Bundle;
import org.osgi.framework.wiring.BundleWiring;

import aQute.bnd.annotation.component.Component;
import aQute.bnd.annotation.component.Reference;
import de.twenty11.skysail.server.app.ApplicationProvider;
import de.twenty11.skysail.server.app.TranslationRenderServiceHolder;
import de.twenty11.skysail.server.core.restlet.ApplicationContextId;
import de.twenty11.skysail.server.core.restlet.RouteBuilder;
import de.twenty11.skysail.server.services.MenuItem;
import de.twenty11.skysail.server.services.MenuItemProvider;

@Component(immediate = true)
public class I18nApplication extends SkysailApplication implements ApplicationProvider, MenuItemProvider {

    private List<TranslationStoreHolder> translationStoreHolders = new ArrayList<>();

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

    @Reference(optional = true, dynamic = true, multiple = true)
    public void addTranslationStoreHolder(TranslationStore service) {
        TranslationStoreHolder holder = new TranslationStoreHolder(service, new HashMap<String, String>());
        this.translationStoreHolders.add(holder);
    }

    public void removeTranslationStoreHolder(TranslationStore service) {
        TranslationStoreHolder holder = new TranslationStoreHolder(service,
                new HashMap<String, String>());
        this.translationStoreHolders.remove(holder);
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
        List<TranslationRenderServiceHolder> translationRenderServices = getApplication().getTranslationRenderServices();
        Optional<Translation> bestTranslation = TranslationUtils.getBestTranslation(translationRenderServices, null, msgKey);

        
        
        
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
    public void setMessage(Message message) {
        translationStoreHolders.stream().forEach(storeHolder -> {
            storeHolder.getStore().get().persist(message.getMsgKey(), message.getMsg(), new Locale("en"), getBundleContext());
        });

    }
}
