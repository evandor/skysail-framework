package io.skysail.server.converter.impl;

import java.util.*;
import java.util.stream.Collectors;

import org.apache.shiro.SecurityUtils;
import org.osgi.framework.Bundle;
import org.osgi.framework.BundleContext;
import org.restlet.data.MediaType;
import org.restlet.representation.StringRepresentation;
import org.restlet.representation.Variant;
import org.restlet.resource.Resource;
import org.stringtemplate.v4.ST;

import io.skysail.api.responses.SkysailResponse;
import io.skysail.api.search.SearchService;
import io.skysail.api.text.Translation;
import io.skysail.server.Constants;
import io.skysail.server.ResourceContextId;
import io.skysail.server.app.SkysailApplication;
import io.skysail.server.caches.Caches;
import io.skysail.server.converter.HtmlConverter;
import io.skysail.server.converter.wrapper.STUserWrapper;
import io.skysail.server.menus.MenuItemProvider;
import io.skysail.server.model.ResourceModel;
import io.skysail.server.rendering.RenderingMode;
import io.skysail.server.rendering.Theme;
import io.skysail.server.restlet.resources.SkysailServerResource;
import io.skysail.server.restlet.response.messages.Message;
import io.skysail.server.services.ThemeDefinition;
import io.skysail.server.stringtemplate.STGroupBundleDir;
import io.skysail.server.utils.CookiesUtils;
import io.skysail.server.utils.RequestUtils;
import lombok.NonNull;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class StringTemplateRenderer {

    private static final String TEMPLATES_DIR = "/templates";

    private static final String INDEX_FOR_MOBILES = "indexMobile";

    private STGroupBundleDir importedGroupBundleDir;
    private Set<MenuItemProvider> menuProviders;
    private HtmlConverter htmlConverter;
    private String indexPageName;

    private SearchService searchService;

    private Resource resource;

    private Theme theme;

    private RenderingMode mode;

    public StringTemplateRenderer(HtmlConverter htmlConverter, Resource resource) {
        this.htmlConverter = htmlConverter;
        this.resource = resource;
    }

    public StringRepresentation createRepresenation(Object entity, Variant target, SkysailServerResource<?> resource) {

        theme = Theme.determineFrom(resource, target);
        mode = CookiesUtils.getModeFromCookie(resource.getRequest());
        
        @SuppressWarnings({ "rawtypes", "unchecked" })
        ResourceModel<SkysailServerResource<?>, ?> resourceModel = new ResourceModel(resource,
                (SkysailResponse<?>) entity, target, theme);
        resourceModel.setSearchService(searchService); // has to be set before
                                                       // menuItemProviders ;(
        resourceModel.setMenuItemProviders(menuProviders);

        STGroupBundleDir.clearUsedTemplates();
        STGroupBundleDir stGroup = createStringTemplateGroup(resource, theme);

        ST index = getStringTemplateIndex(resource, stGroup);

        addSubstitutions(resourceModel, index);

        checkForInspection(resource, index);

        return createRepresentation(index, stGroup);
    }

    private STGroupBundleDir createStringTemplateGroup(Resource resource, Theme theme) {
        SkysailApplication currentApplication = (SkysailApplication) resource.getApplication();
        Bundle appBundle = currentApplication.getBundle();
        if (appBundle == null) {
            log.warn("could not determine bundle of current ApplicationModel {}, follow-up errors might occur",
                    currentApplication.getName());
        }
        if (appBundle.getResource(TEMPLATES_DIR) != null) {
            STGroupBundleDir stGroup = new STGroupBundleDir(appBundle, resource, TEMPLATES_DIR);
            importTemplates("skysail.server.converter", resource, appBundle, TEMPLATES_DIR, stGroup, theme);

            String productBundleName = System.getProperty(Constants.PRODUCT_BUNDLE_IDENTIFIER);
            importTemplates(productBundleName, resource, appBundle, TEMPLATES_DIR, stGroup, theme);

            return stGroup;

        } else {
            Optional<Bundle> thisBundle = findBundle(appBundle.getBundleContext(), "skysail.server.converter");
            STGroupBundleDir stGroup = new STGroupBundleDir(thisBundle.get(), resource, TEMPLATES_DIR);

            String productBundleName = System.getProperty(Constants.PRODUCT_BUNDLE_IDENTIFIER);
            importTemplates(productBundleName, resource, appBundle, TEMPLATES_DIR, stGroup, theme);

            return stGroup;
        }
    }

    private ST getStringTemplateIndex(Resource resource, STGroupBundleDir stGroup) {
        if (resource.getContext() != null
                && resource.getContext().getAttributes().containsKey(ResourceContextId.RENDERER_HINT.name())) {
            String root = (String) resource.getContext().getAttributes().get(ResourceContextId.RENDERER_HINT.name());
            resource.getContext().getAttributes().remove(ResourceContextId.RENDERER_HINT.name());
            return stGroup.getInstanceOf(root);
        }

        if (RequestUtils.isMobile(resource.getRequest())) {
            indexPageName = INDEX_FOR_MOBILES;
        } else {
            indexPageName = CookiesUtils.getMainPageFromCookie(resource.getRequest());
        }
        if (indexPageName != null && indexPageName.length() > 0) {
            return stGroup.getInstanceOf(indexPageName);
        } else {
            return stGroup.getInstanceOf("index");
        }
    }

    private StringRepresentation createRepresentation(ST index, STGroupBundleDir stGroup) {
        String stringTemplateRenderedHtml = index.render();

        // index.getEvents()

        if (importedGroupBundleDir != null) {
            stGroup.addUsedTemplates(importedGroupBundleDir.getUsedTemplates());
        }
        String templatesHtml = isDebug() ? getTemplatesHtml(stGroup) : "";
        StringRepresentation rep = new StringRepresentation(
                stringTemplateRenderedHtml.replace("%%templates%%", templatesHtml));

        rep.setMediaType(MediaType.TEXT_HTML);
        return rep;
    }

    public boolean isDebug() {
        return mode.equals(RenderingMode.DEBUG);
    }

    public boolean isEdit() {
        return "edit".equalsIgnoreCase(theme.getOption().toString());
    }

    /**
     * As the templates used for creating the output cannot be added to the
     * output itself during creation time, they are added in an additional step
     * here
     *
     * @param stGroup
     */
    private String getTemplatesHtml(STGroupBundleDir stGroup) {
        StringBuilder sb = new StringBuilder();
        sb.append(stGroup.toString().replace("\n", "<br>\n")).append("\n<hr>");
        String templates = stGroup.getUsedTemplates().stream().map(template -> "<li>" + template + "</li>")
                .collect(Collectors.joining("\n"));
        sb.append("<ul>").append(templates).append("</ul>");
        return sb.toString();
    }

    private void addSubstitutions(ResourceModel<SkysailServerResource<?>, ?> resourceModel, @NonNull ST decl) {

        SkysailServerResource<?> resource = resourceModel.getResource();

        String installationFromCookie = CookiesUtils.getInstallationFromCookie(resource.getRequest());

        decl.add("user", new STUserWrapper(SecurityUtils.getSubject(), installationFromCookie));
        decl.add("converter", this);

        Map<String, Translation> messages = resource.getMessages(resourceModel.getFields());
        messages.put("productName", new Translation(getProductName(), null, Collections.emptySet()));
        messages.put("productVersion", new Translation("1.2.3", null, Collections.emptySet()));

        decl.add("messages", messages);
        decl.add("model", resourceModel);
    }

    public List<Notification> getNotifications() {
        List<Notification> notifications = htmlConverter.getNotifications();
        String messageIds = resource.getOriginalRef().getQueryAsForm().getFirstValue("msgIds");
        if (messageIds != null) {
            List<Message> messages = Arrays.stream(messageIds.split("|")).map(id -> getMessageFromCache(id))
                    .filter(msg -> msg != null).collect(Collectors.toList());
            for (Message message : messages) {
                notifications.add(new Notification(message.getMsg(), "success"));
            }
        }
        String errorMessage = resource.getAttribute("message.error");
        if (errorMessage != null) {
            notifications.add(new Notification(errorMessage, "error"));
        }
        return notifications;
    }

    private Message getMessageFromCache(String id) {
        //CacheStats messageCacheStats = Caches.getMessageCacheStats();
        //System.out.println(messageCacheStats);
        return Caches.getMessageCache().getIfPresent(Long.valueOf(id));
    }

    public List<String> getPeitybars() {
        return htmlConverter.getPeitybars();
    }

    private void importTemplates(String symbolicName, Resource resource, Bundle appBundle, String resourcePath,
            STGroupBundleDir stGroup, Theme theme) {
        Optional<Bundle> theBundle = findBundle(appBundle.getBundleContext(), symbolicName);
        if (theBundle.isPresent()) {
            String mediaTypedResourcePath = (resourcePath + "/" + theme).replace("/*", "");
            importTemplates(resource, mediaTypedResourcePath, stGroup, theBundle);
            importTemplates(resource, mediaTypedResourcePath + "/head", stGroup, theBundle);
            importTemplates(resource, mediaTypedResourcePath + "/navigation", stGroup, theBundle);
            importTemplates(resource, resourcePath + "/common", stGroup, theBundle);
            importTemplates(resource, resourcePath + "/common/head", stGroup, theBundle);
            importTemplates(resource, resourcePath + "/common/navigation", stGroup, theBundle);
        }
    }

    private void importTemplates(Resource resource, String resourcePath, STGroupBundleDir stGroup,
            Optional<Bundle> theBundle) {
        if (resourcePathExists(resourcePath, theBundle)) {
            importedGroupBundleDir = new STGroupBundleDir(theBundle.get(), resource, resourcePath);
            stGroup.importTemplates(importedGroupBundleDir);
            log.debug("importing templates from {}: '{}'", theBundle.get().getSymbolicName(), resourcePath);
        }
    }

    private static boolean resourcePathExists(String resourcePath, Optional<Bundle> theBundle) {
        return theBundle.get().getResource(resourcePath) != null;
    }

    private synchronized String getProductName() {
        if (htmlConverter == null) {
            return "Skysail";
        }
        return htmlConverter.getProductName();
    }

    private Optional<Bundle> findBundle(BundleContext bundleContext, String bundleName) {
        Bundle[] bundles = bundleContext.getBundles();
        Optional<Bundle> thisBundle = Arrays.stream(bundles).filter(b -> b.getSymbolicName().equals(bundleName))
                .findFirst();
        return thisBundle;
    }

    public void setMenuProviders(Set<MenuItemProvider> menuProviders) {
        this.menuProviders = menuProviders;

    }

    private void checkForInspection(Resource resource, ST index) {
        Object inspect = resource.getRequest().getAttributes().get(SkysailServerResource.INSPECT_PARAM_NAME);
        if (resource.getHostRef().getHostDomain().contains("localhost") && inspect != null) {
            index.inspect();
        }
    }

    public void setSearchService(SearchService searchService) {
        this.searchService = searchService;
    }

    public List<ThemeDefinition> getThemes() {
        List<ThemeDefinition> themeDefs = htmlConverter.getThemeProviders().stream().map(p -> p.getTheme())
                .collect(Collectors.toList());
        themeDefs.stream().forEach(td -> setIsSelected(td)); // NOSONAR
        return themeDefs;
    }

    private void setIsSelected(ThemeDefinition themeDef) {
        themeDef.setSelected(false);
        if (themeDef.getName().equalsIgnoreCase(theme.getVariant().name())) {
            themeDef.setSelected(true);
        }
    }

}
