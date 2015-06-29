package io.skysail.server.text.store.bundleresource.impl;

import io.skysail.api.text.*;
import io.skysail.server.app.SkysailApplication;
import io.skysail.server.restlet.resources.SkysailServerResource;
import io.skysail.server.text.TranslationStoreHolder;
import io.skysail.server.utils.TranslationUtils;

import java.lang.ref.WeakReference;
import java.util.*;
import java.util.stream.Collectors;

import org.osgi.framework.Bundle;
import org.osgi.framework.wiring.BundleWiring;

import aQute.bnd.annotation.component.*;
import de.twenty11.skysail.server.app.*;
import de.twenty11.skysail.server.core.restlet.*;
import de.twenty11.skysail.server.services.*;

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

    /**
     * get a message with the "best" translation (i.e. the first one returned), augmented with the
     * information about which other translation stores are available.
     * @param selectedStore 
     */
    public Message getMessage(String key, String selectedStore, SkysailServerResource<?> resource) {
        Set<TranslationStoreHolder> translationStores = serviceListProviderRef.get().getTranslationStores();
        Set<TranslationRenderServiceHolder> rendererServices = serviceListProviderRef.get().getTranslationRenderServices();
        if (selectedStore != null) {
            Translation translation = TranslationUtils.getTranslation(key, translationStores, selectedStore, resource);
            if (translation != null) {
                return new Message(key, translation, getPreferedRenderer(rendererServices, translation.getValue()));
            }
        }
        List<Translation> translations = TranslationUtils.getAllTranslations(translationStores, key, resource);
        if (translations.size() == 0) {
            Set<String> storeNames = translationStores.stream().map(ts -> {
                return ts.getName();
            }).collect(Collectors.toSet());
            Translation translation = new Translation("", translationStores.iterator().next().getStore().get(),
                    storeNames);
            return new Message(key, translation, getPreferedRenderer(rendererServices, ""));
        }
        return new Message(key, translations.get(0), getPreferedRenderer(rendererServices, translations.get(0).getValue()));
    }

    private TranslationRenderService getPreferedRenderer(Set<TranslationRenderServiceHolder> rendererServices, String translationValue) {
       return rendererServices.stream().filter(rs -> {
            return rs.getService().get().applicable(translationValue);
        }).map(TranslationRenderServiceHolder::getService).map(WeakReference::get).findFirst().orElse(null);
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
            TranslationRenderService preferredRenderer = message.getPreferredRenderer();
            String text = preferredRenderer != null ? preferredRenderer.addRendererInfo() + message.getMsg() : message.getMsg();
            storeHolder.getStore().get().persist(message.getMsgKey(), text, new Locale("en"), getBundleContext());
        });

    }
}
