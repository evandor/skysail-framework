package io.skysail.server.converter;

import io.skysail.api.favorites.FavoritesService;
import io.skysail.server.converter.impl.StringTemplateRenderer;
import io.skysail.server.restlet.resources.SkysailServerResource;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.CopyOnWriteArrayList;

import lombok.extern.slf4j.Slf4j;

import org.apache.shiro.SecurityUtils;
import org.osgi.service.event.Event;
import org.osgi.service.event.EventHandler;
import org.restlet.data.MediaType;
import org.restlet.engine.converter.ConverterHelper;
import org.restlet.engine.resource.VariantInfo;
import org.restlet.representation.Representation;
import org.restlet.representation.StringRepresentation;
import org.restlet.representation.Variant;
import org.restlet.resource.Resource;

import aQute.bnd.annotation.component.Component;
import aQute.bnd.annotation.component.Reference;
import de.twenty11.skysail.server.app.SkysailApplication;
import de.twenty11.skysail.server.core.osgi.EventHelper;
import de.twenty11.skysail.server.services.MenuItemProvider;
import de.twenty11.skysail.server.services.OsgiConverterHelper;
import etm.core.configuration.EtmManager;
import etm.core.monitor.EtmMonitor;
import etm.core.monitor.EtmPoint;

/**
 * A component providing converting functionality via the StringTemplate
 * library.
 *
 * TODO get rid of shiro dependency TODO package structure
 *
 */
@Component(immediate = true, properties = { "event.topics=" + EventHelper.GUI_MSG + "/*" })
@Slf4j
public class HtmlConverter extends ConverterHelper implements OsgiConverterHelper, EventHandler {

    protected static final EtmMonitor etmMonitor = EtmManager.getEtmMonitor();

    private static final float DEFAULT_MATCH_VALUE = 0.5f;
    private static Map<MediaType, Float> mediaTypesMatch = new HashMap<MediaType, Float>();

    private String templateNameFromCookie;
    private List<Event> events = new CopyOnWriteArrayList<>();
    private volatile Set<MenuItemProvider> menuProviders = new HashSet<>();

    private Resource resource;

    private volatile FavoritesService favoritesService;

    static {
        mediaTypesMatch.put(MediaType.TEXT_HTML, 0.95F);
        mediaTypesMatch.put(SkysailApplication.SKYSAIL_TREE_FORM, 1.0F);
    }

    // --- Menu Providers ------------------------------------------------

    @Reference(multiple = true, optional = true, dynamic = true)
    public void addMenuProvider(MenuItemProvider provider) {
        if (provider == null) { // || provider.getMenuEntries() == null) {
            return;
        }
        menuProviders.add(provider);
    }

    public void removeMenuProvider(MenuItemProvider provider) {
        menuProviders.remove(provider);
    }

    public Set<MenuItemProvider> getMenuProviders() {
        return Collections.unmodifiableSet(menuProviders);
    }

    // --- Favorites Service ------------------------------------------------

    @Reference(multiple = false, optional = true, dynamic = true)
    public void setFavoritesService(FavoritesService service) {
        this.favoritesService = service;
    }

    public void unsetFavoritesService(FavoritesService service) {
        this.favoritesService = null;
    }

    @Override
    public List<Class<?>> getObjectClasses(Variant source) {
        throw new RuntimeException("getObjectClasses method is not implemented yet");
    }

    @Override
    public List<VariantInfo> getVariants(Class<?> source) {
        return Arrays.asList(new VariantInfo(SkysailApplication.SKYSAIL_TREE_FORM));
    }

    @Override
    public float score(Object source, Variant target, Resource resource) {
        if (target == null) {
            return 0.0f;
        }
        for (MediaType mediaType : mediaTypesMatch.keySet()) {
            if (target.getMediaType().equals(mediaType)) {
                log.info("converter '{}' matched '{}' with threshold {}", new Object[] {
                        this.getClass().getSimpleName(), mediaTypesMatch, mediaTypesMatch.get(mediaType) });
                return mediaTypesMatch.get(mediaType);
            }
        }
        return DEFAULT_MATCH_VALUE;
    }

    @Override
    public <T> float score(Representation source, Class<T> target, Resource resource) {
        return -1.0F;
    }

    @Override
    public <T> T toObject(Representation source, Class<T> target, Resource resource) {
        throw new RuntimeException("toObject method is not implemented yet");
    }

    /**
     * source: List of entities variant: e.g. [text/html] resource: ? extends
     * SkysailServerResource
     */
    @Override
    public Representation toRepresentation(Object originalSource, Variant target, Resource resource) {
        EtmPoint point = etmMonitor.createPoint(this.getClass().getSimpleName() + ":toRepresentation");

        this.resource = resource;

        StringTemplateRenderer stringTemplateRenderer = new StringTemplateRenderer(this);
        stringTemplateRenderer.setMenuProviders(menuProviders);
        stringTemplateRenderer.setFavoritesService(favoritesService);
        StringRepresentation rep = stringTemplateRenderer.createRepresenation(originalSource, target,
                (SkysailServerResource<?>) resource);

        point.collect();
        return rep;
    }

    public boolean isDebug() {
        return "debug".equalsIgnoreCase(templateNameFromCookie);
    }

    public boolean isEdit() {
        return "edit".equalsIgnoreCase(templateNameFromCookie);
    }

    @Override
    public void handleEvent(Event event) {
        events.add(event);
    }

    public List<Notification> getNotifications() {
        String currentUser = SecurityUtils.getSubject().getPrincipal().toString();
        if (currentUser == null) {
            return Collections.emptyList();
        }
//        if (!(resource.getRequest().getMethod().equals(Method.GET))) {
//            return Collections.emptyList();
//        }
        List<Notification> result = new ArrayList<>();
        events.stream().forEach(e -> {
            String msg = (String) e.getProperty("msg");
            String type = (String) e.getProperty("type");
            result.add(new Notification(msg, type));
            events.remove(e);
        });
        return result;
    }

}
