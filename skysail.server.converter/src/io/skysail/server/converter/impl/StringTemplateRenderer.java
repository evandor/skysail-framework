package io.skysail.server.converter.impl;

import io.skysail.api.favorites.FavoritesService;
import io.skysail.api.peers.PeersProvider;
import io.skysail.api.responses.SkysailResponse;
import io.skysail.server.app.SkysailApplication;
import io.skysail.server.converter.HtmlConverter;
import io.skysail.server.converter.Notification;
import io.skysail.server.converter.stringtemplate.STGroupBundleDir;
import io.skysail.server.converter.wrapper.STUserWrapper;
import io.skysail.server.model.ResourceModel;
import io.skysail.server.restlet.resources.SkysailServerResource;
import io.skysail.server.utils.RequestUtils;

import java.net.URL;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

import lombok.NonNull;
import lombok.extern.slf4j.Slf4j;

import org.apache.shiro.SecurityUtils;
import org.osgi.framework.Bundle;
import org.restlet.data.MediaType;
import org.restlet.representation.StringRepresentation;
import org.restlet.representation.Variant;
import org.restlet.resource.Resource;
import org.stringtemplate.v4.ST;

import de.twenty11.skysail.server.core.restlet.ResourceContextId;
import de.twenty11.skysail.server.core.restlet.utils.CookiesUtils;
import de.twenty11.skysail.server.services.MenuItemProvider;

@Slf4j
public class StringTemplateRenderer {

    public static final String INDEX_FOR_MOBILES = "indexMobile";
    
    private STGroupBundleDir importedGroupBundleDir;
    private Set<MenuItemProvider> menuProviders;
    private String templateFromCookie;
    private HtmlConverter htmlConverter;
    private FavoritesService favoritesService;
    private PeersProvider peersProvider;
    private String indexPageName;

    public StringTemplateRenderer(HtmlConverter htmlConverter) {
        this.htmlConverter = htmlConverter;
    }

    public StringRepresentation createRepresenation(Object entity, Variant target,
            SkysailServerResource<?> resource) {
        
        @SuppressWarnings({ "rawtypes", "unchecked" })
        ResourceModel<SkysailServerResource<?>,?> resourceModel = new ResourceModel(resource, (SkysailResponse<?>)entity, target);
        resourceModel.setFavoritesService(favoritesService);
        resourceModel.setMenuItemProviders(menuProviders);

        templateFromCookie = CookiesUtils.getTemplateFromCookie(resource.getRequest());

        STGroupBundleDir stGroup = createSringTemplateGroup(resource, target.getMediaType().getName());
        
        ST index = getStringTemplateIndex(resource, stGroup);
        
        addSubstitutions(resourceModel, index);
        
        checkForInspection(resource, index);

        return createRepresentation(index, stGroup);
    }

    private STGroupBundleDir createSringTemplateGroup(Resource resource, String mediaType) {
        SkysailApplication currentApplication = (SkysailApplication) resource.getApplication();
        Bundle appBundle = currentApplication.getBundle();
        String resourcePath = ("/templates/" + mediaType).replace("/*", "");
        log.info("reading templates from resource path '{}'", resourcePath);
        URL templatesResource = appBundle.getResource("/templates");
        if (templatesResource != null) {
            STGroupBundleDir stGroup = new STGroupBundleDir(appBundle, resource, "/templates");
            importTemplate("skysail.server.converter", resource, appBundle, resourcePath, stGroup);
            importTemplate("skysail.server.documentation", resource, appBundle, resourcePath, stGroup);
            return stGroup;

        } else {
            Optional<Bundle> thisBundle = findBundle(appBundle, "skysail.server.converter");
            return new STGroupBundleDir(thisBundle.get(), resource, resourcePath);
        }
    }

    private ST getStringTemplateIndex(Resource resource, STGroupBundleDir stGroup) {
        if (resource.getContext().getAttributes().containsKey(ResourceContextId.RENDERER_HINT.name())) {
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

        if (importedGroupBundleDir != null) {
            stGroup.addUsedTemplates(importedGroupBundleDir.getUsedTemplates());
        }
        String templatesHtml = isDebug() ? getTemplatesHtml(stGroup) : "";
        StringRepresentation rep = new StringRepresentation(stringTemplateRenderedHtml.replace("%%templates%%",
                templatesHtml));

        rep.setMediaType(MediaType.TEXT_HTML);
        return rep;
    }

    public boolean isDebug() {
        return "debug".equalsIgnoreCase(templateFromCookie);
    }

    public boolean isEdit() {
        return "edit".equalsIgnoreCase(templateFromCookie);
    }

   
    /**
     * As the templates used for creating the output cannot be added to the
     * output itself during creation time, they are added in an additional step
     * here
     * 
     * @param stGroup
     */
    private String getTemplatesHtml(STGroupBundleDir stGroup) {
        return stGroup.getUsedTemplates().stream().map(template -> "<li>" + template + "</li>")
                .collect(Collectors.joining("\n"));
    }

    @SuppressWarnings("unchecked")
    private void addSubstitutions(ResourceModel<SkysailServerResource<?>,?> resourceModel, @NonNull ST decl) {

        SkysailServerResource<?> resource = resourceModel.getResource();
        
        String installationFromCookie = CookiesUtils.getInstallationFromCookie(resource.getRequest());

        decl.add("user", new STUserWrapper(SecurityUtils.getSubject(), peersProvider, installationFromCookie));
        decl.add("converter", this);
        
        Map<String, String> messages = resource.getMessages(resourceModel.getFields());
        messages.put("productName", getProductName());
        
        decl.add("messages", messages);
        decl.add("model", resourceModel);
    }

    public List<Notification> getNotifications() {
        return htmlConverter.getNotifications();
    }

    private void importTemplate(String symbolicName, Resource resource, Bundle appBundle, String resourcePath,
            STGroupBundleDir stGroup) {
        Optional<Bundle> theBundle = findBundle(appBundle, symbolicName);
        if (theBundle.isPresent()) {
            if (theBundle.get().getResource(resourcePath) != null) {
                importedGroupBundleDir = new STGroupBundleDir(theBundle.get(), resource, resourcePath);
                stGroup.importTemplates(importedGroupBundleDir);
            }
        }
    }

    private String getProductName() {
        return "Skysail";
    }

    private Optional<Bundle> findBundle(Bundle bundle, String bundleName) {
        Bundle[] bundles = bundle.getBundleContext().getBundles();
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

    public void setFavoritesService(FavoritesService favoritesService) {
        this.favoritesService = favoritesService;
    }

    public void setPeersProvider(PeersProvider peersProvider) {
        this.peersProvider = peersProvider;
    }
    
    

}
