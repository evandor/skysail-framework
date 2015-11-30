package io.skysail.server.converter;

import java.util.*;
import java.util.concurrent.CopyOnWriteArrayList;

import org.apache.shiro.SecurityUtils;
import org.osgi.service.component.annotations.*;
import org.osgi.service.event.*;
import org.restlet.data.MediaType;
import org.restlet.engine.converter.ConverterHelper;
import org.restlet.engine.resource.VariantInfo;
import org.restlet.representation.*;
import org.restlet.resource.Resource;

import de.twenty11.skysail.server.core.osgi.EventHelper;
import de.twenty11.skysail.server.services.OsgiConverterHelper;
import etm.core.configuration.EtmManager;
import etm.core.monitor.*;
import io.skysail.api.peers.PeersProvider;
import io.skysail.api.search.SearchService;
import io.skysail.server.app.SkysailApplication;
import io.skysail.server.converter.impl.*;
import io.skysail.server.menus.MenuItemProvider;
import io.skysail.server.restlet.resources.SkysailServerResource;
import lombok.extern.slf4j.Slf4j;

/**
 * A component providing converting functionality via the StringTemplate
 * library.
 *
 * TODO get rid of shiro dependency TODO package structure
 *
 */
@Component(immediate = true, property = { "event.topics=" + EventHelper.GUI +"/*"})
@Slf4j
public class HtmlConverter extends ConverterHelper implements OsgiConverterHelper, EventHandler {

    protected static final EtmMonitor etmMonitor = EtmManager.getEtmMonitor();

    private static final float DEFAULT_MATCH_VALUE = 0.5f;
    private static Map<MediaType, Float> mediaTypesMatch = new HashMap<MediaType, Float>();

    private String templateNameFromCookie;
    private List<Event> events = new CopyOnWriteArrayList<>();
    private List<Event> peityBarEvents= new CopyOnWriteArrayList<>();

    private volatile Set<MenuItemProvider> menuProviders = new HashSet<>();
    private volatile PeersProvider peersProvider;

    //private InstallationProvider installationProvider;

    private SearchService searchService;

    static {
        mediaTypesMatch.put(MediaType.TEXT_HTML, 0.95F);
        mediaTypesMatch.put(SkysailApplication.SKYSAIL_TREE_FORM, 1.0F);
        //mediaTypesMatch.put(SkysailApplication.SKYSAIL_MAILTO_MEDIATYPE, 1.0F);
        mediaTypesMatch.put(SkysailApplication.SKYSAIL_TIMELINE_MEDIATYPE, 1.0F);
    }

    // --- Menu Providers ------------------------------------------------

    @Reference(policy = ReferencePolicy.DYNAMIC, cardinality = ReferenceCardinality.MULTIPLE)
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


    // --- Search Service ------------------------------------------------

    @Reference(policy = ReferencePolicy.DYNAMIC, cardinality = ReferenceCardinality.OPTIONAL)
    public void setSearchService(SearchService service) {
        this.searchService = service;
    }

    public void unsetSearchService(SearchService service) {
        this.searchService = null;
    }

    // --- Peers Provider Service ------------------------------------------------

    @Reference(policy = ReferencePolicy.DYNAMIC, cardinality = ReferenceCardinality.OPTIONAL)
    public void setPeersProvider(PeersProvider service) {
        this.peersProvider = service;
    }

    public void unsetPeersProvider(PeersProvider service) {
        this.peersProvider = null;
    }

    @Override
    public List<Class<?>> getObjectClasses(Variant source) {
        throw new RuntimeException("getObjectClasses method is not implemented yet");
    }

    @Override
    public List<VariantInfo> getVariants(Class<?> source) {
        return Arrays.asList(
                new VariantInfo(SkysailApplication.SKYSAIL_TREE_FORM),
                new VariantInfo(SkysailApplication.SKYSAIL_MAILTO_MEDIATYPE),
                new VariantInfo(SkysailApplication.SKYSAIL_TIMELINE_MEDIATYPE)
        );
    }

    @Override
    public float score(Object source, Variant target, Resource resource) {
        if (target == null) {
            return 0.0f;
        }
        for (MediaType mediaType : mediaTypesMatch.keySet()) {
            if (target.getMediaType().equals(mediaType)) {
                log.debug("converter '{}' matched '{}' with threshold {}", new Object[] {
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

        StringTemplateRenderer stringTemplateRenderer = new StringTemplateRenderer(this);
        stringTemplateRenderer.setMenuProviders(menuProviders);
        stringTemplateRenderer.setPeersProvider(peersProvider);
        stringTemplateRenderer.setSearchService(searchService);
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
        if (event.getTopic().startsWith(EventHelper.GUI_MSG)) {
            events.add(event);
        } else if (event.getTopic().startsWith(EventHelper.GUI_PEITY_BAR)) {
            peityBarEvents.add(event);
        }
    }

    public List<Notification> getNotifications() {
        String currentUser = SecurityUtils.getSubject().getPrincipal().toString();
        if (currentUser == null) {
            return Collections.emptyList();
        }
        List<Notification> result = new ArrayList<>();
        events.stream().forEach(e -> {
            String msg = (String) e.getProperty("msg");
            String type = (String) e.getProperty("type");
            result.add(new Notification(msg, type));
            events.remove(e);
        });
        return result;
    }

    public List<String> getPeitybars() {
        List<String> result = new ArrayList<>();
        peityBarEvents.stream().forEach(e -> {
            String msg = (String) e.getProperty("msg");
            result.add(msg);
            //events.remove(e);
        });
        return result;
    }

    public String getProductName() {
        return "Skysail";
    }

}
