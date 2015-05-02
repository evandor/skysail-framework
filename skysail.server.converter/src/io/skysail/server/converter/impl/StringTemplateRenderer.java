package io.skysail.server.converter.impl;

import io.skysail.api.favorites.FavoritesService;
import io.skysail.api.links.Link;
import io.skysail.api.peers.PeersProvider;
import io.skysail.server.app.SkysailApplication;
import io.skysail.server.converter.*;
import io.skysail.server.converter.stringtemplate.STGroupBundleDir;
import io.skysail.server.converter.wrapper.*;
import io.skysail.server.restlet.SourceWrapper;
import io.skysail.server.restlet.resources.*;

import java.net.URL;
import java.util.*;
import java.util.stream.Collectors;

import lombok.extern.slf4j.Slf4j;

import org.apache.shiro.SecurityUtils;
import org.osgi.framework.Bundle;
import org.restlet.data.MediaType;
import org.restlet.representation.*;
import org.restlet.resource.Resource;
import org.stringtemplate.v4.ST;

import de.twenty11.skysail.server.core.FormField;
import de.twenty11.skysail.server.core.restlet.ResourceContextId;
import de.twenty11.skysail.server.core.restlet.utils.CookiesUtils;
import de.twenty11.skysail.server.services.MenuItemProvider;

@Slf4j
public class StringTemplateRenderer {

    private STGroupBundleDir importedGroupBundleDir;
    private Set<MenuItemProvider> menuProviders;
    private String templateFromCookie;
    private HtmlConverter htmlConverter;
    private FavoritesService favoritesService;
    private PeersProvider peersProvider;

    public StringTemplateRenderer(HtmlConverter htmlConverter) {
        this.htmlConverter = htmlConverter;
    }

    public StringRepresentation createRepresenation(Object originalSource, Variant target,
            SkysailServerResource<?> resource) {

        templateFromCookie = CookiesUtils.getTemplateFromCookie(resource.getRequest());

        SourceWrapper sourceWrapper = new SourceWrapper(originalSource, target, resource);
        STGroupBundleDir stGroup = createSringTemplateGroup(resource, target.getMediaType().getName());
        ST index = getStringTemplateIndex(resource, stGroup);

        addAssociatedLinks(resource, sourceWrapper);
        addSubstitutions(sourceWrapper.getConvertedSource(), resource, index, target, menuProviders);
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
            String root = (String)resource.getContext().getAttributes().get(ResourceContextId.RENDERER_HINT.name());
            resource.getContext().getAttributes().remove(ResourceContextId.RENDERER_HINT.name());
            return stGroup.getInstanceOf(root);
        }

        String mainPage = CookiesUtils.getMainPageFromCookie(resource.getRequest());
        if (mainPage != null && mainPage.length() > 0) {
            return stGroup.getInstanceOf(mainPage);
        } else {
            return stGroup.getInstanceOf("index");
        }
    }

    private void addAssociatedLinks(Resource resource, SourceWrapper sourceWrapper) {
        if (!(resource instanceof ListServerResource)) {
            return;
        }
        ListServerResource<?> listServerResource = (ListServerResource<?>) resource;
        List<Link> links = listServerResource.getLinks();
        List<Class<? extends SkysailServerResource<?>>> entityResourceClass = listServerResource
                .getAssociatedServerResources();
        if (entityResourceClass != null && sourceWrapper.getConvertedSource() instanceof List) {
            // EntityServerResource<?> esr =
            // ResourceUtils.createEntityServerResource(entityResourceClass,
            // resource);

            List<?> sourceAsList = (List<?>) sourceWrapper.getConvertedSource();
            for (Object object : sourceAsList) {
                if (!(object instanceof Map)) {
                    continue;
                }
                String id = guessId(object);
                if (id == null) {
                    continue;
                }

                String linkshtml = links
                        .stream()
                        .filter(l -> id.equals(l.getRefId()))
                        .map(link -> {
                            StringBuilder sb = new StringBuilder();

                            if (link.getImage(MediaType.TEXT_HTML) != null) {
                                sb.append("<a href='")
                                        .append(link.getUri())
                                        .append("' title='")
                                        .append(link.getTitle())
                                        .append("'>")
                                        .append("<span class='glyphicon glyphicon-"
                                                + link.getImage(MediaType.TEXT_HTML) + "' aria-hidden='true'></span>")
                                        .append("</a>");
                            } else {
                                sb.append("<a href='").append(link.getUri()).append("'>").append(link.getTitle())
                                        .append("</a>");
                            }
                            return sb.toString();
                        }).collect(Collectors.joining("&nbsp;&nbsp;"));

                ((Map<String, Object>) object).put("_links", linkshtml);
            }
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

    private String guessId(Object object) {
        if (!(object instanceof Map))
            return "";
        Map<String, Object> entity = ((Map<String, Object>) object);

        if (entity.get("id") != null) {
            Object value = entity.get("id");
            return value.toString().replace("#", "");
        } else if (entity.get("@rid") != null) {
            String str = entity.get("@rid").toString();
            return (str.replace("#", ""));
        } else {
            return "";
        }
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
    private void addSubstitutions(Object source, SkysailServerResource<?> resource, ST decl, Variant target,
            Set<MenuItemProvider> menuProviders) {

        String installationFromCookie = CookiesUtils.getInstallationFromCookie(resource.getRequest());

        decl.add("user", new STUserWrapper(SecurityUtils.getSubject(), peersProvider, installationFromCookie));
        decl.add("target", new STTargetWrapper(target));
        decl.add("converter", this);
        decl.add("services", new STServicesWrapper(menuProviders, null, resource));
        decl.add("resource", new StResourceWrapper(source, resource, favoritesService));

        if (source instanceof List) {
            decl.add("source", new STListSourceWrapper((List<Object>) source));
        } else {
            decl.add("source", new STSourceWrapper(source));
        }

        List<FormField> fields = null;

        FieldFactory fieldFactory = FieldsFactory.getFactory(source, resource);
        log.info("using factory '{}' for {}-Source: {}", new Object[] { fieldFactory.getClass().getSimpleName(),
                source.getClass().getSimpleName(), source });
        try {
            fields = fieldFactory.determineFrom(resource);
        } catch (Exception e) {
            e.printStackTrace();
        }
        decl.add("fields", new STFieldsWrapper(fields));

        Map<String, String> messages = resource.getMessages(fields);
        messages.put("productName", getProductName());
        decl.add("messages", messages);
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
        String inspect = CookiesUtils.getInpsectFromCookie(resource.getRequest());
        if (resource.getHostRef().getHostDomain().contains("localhost") && inspect != null
                && inspect.equalsIgnoreCase("on")) {
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
