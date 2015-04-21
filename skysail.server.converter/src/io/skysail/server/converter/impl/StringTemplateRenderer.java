package io.skysail.server.converter.impl;

import io.skysail.api.favorites.FavoritesService;
import io.skysail.api.forms.Reference;
import io.skysail.api.links.Link;
import io.skysail.api.responses.SkysailResponse;
import io.skysail.server.app.SkysailApplication;
import io.skysail.server.converter.HtmlConverter;
import io.skysail.server.converter.Notification;
import io.skysail.server.converter.stringtemplate.STGroupBundleDir;
import io.skysail.server.converter.wrapper.STFieldsWrapper;
import io.skysail.server.converter.wrapper.STListSourceWrapper;
import io.skysail.server.converter.wrapper.STServicesWrapper;
import io.skysail.server.converter.wrapper.STSourceWrapper;
import io.skysail.server.converter.wrapper.STTargetWrapper;
import io.skysail.server.converter.wrapper.STUserWrapper;
import io.skysail.server.converter.wrapper.StResourceWrapper;
import io.skysail.server.forms.ListView;
import io.skysail.server.forms.PostView;
import io.skysail.server.forms.Visibility;
import io.skysail.server.restlet.resources.EntityServerResource;
import io.skysail.server.restlet.resources.ListServerResource;
import io.skysail.server.restlet.resources.PostEntityServerResource;
import io.skysail.server.restlet.resources.SkysailServerResource;
import io.skysail.server.utils.ReflectionUtils;
import io.skysail.server.utils.ResourceUtils;

import java.lang.reflect.Field;
import java.net.URL;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

import lombok.extern.slf4j.Slf4j;

import org.apache.commons.beanutils.DynaProperty;
import org.apache.shiro.SecurityUtils;
import org.osgi.framework.Bundle;
import org.restlet.data.MediaType;
import org.restlet.representation.StringRepresentation;
import org.restlet.representation.Variant;
import org.restlet.resource.Resource;
import org.stringtemplate.v4.ST;

import de.twenty11.skysail.server.app.SourceWrapper;
import de.twenty11.skysail.server.beans.DynamicEntity;
import de.twenty11.skysail.server.beans.EntityDynaProperty;
import de.twenty11.skysail.server.core.FormField;
import de.twenty11.skysail.server.core.restlet.utils.CookiesUtils;
import de.twenty11.skysail.server.services.MenuItemProvider;

@Slf4j
public class StringTemplateRenderer {

    private STGroupBundleDir importedGroupBundleDir;
    private Set<MenuItemProvider> menuProviders;
    private String templateFromCookie;
    private HtmlConverter htmlConverter;
    private FavoritesService favoritesService;

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
        String mainPage = CookiesUtils.getMainPageFromCookie(resource.getRequest());
        ST index;
        if (mainPage != null && mainPage.length() > 0) {
            index = stGroup.getInstanceOf(mainPage);
        } else {
            index = stGroup.getInstanceOf("index");
        }
        if (index == null) {
            throw new IllegalStateException("cannot get instance of stringtemplate 'index'");
        }
        return index;
    }

    private void addAssociatedLinks(Resource resource, SourceWrapper sourceWrapper) {
        if (!(resource instanceof ListServerResource)) {
            return;
        }
        ListServerResource<?> listServerResource = (ListServerResource<?>) resource;
        List<Link> links = listServerResource.getLinks();
        Class<? extends EntityServerResource<?>> entityResourceClass = listServerResource.getAssociatedEntityResource();
        if (entityResourceClass != null && sourceWrapper.getConvertedSource() instanceof List) {
            EntityServerResource<?> esr = ResourceUtils.createEntityServerResource(entityResourceClass, resource);

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

        decl.add("user", new STUserWrapper(SecurityUtils.getSubject()));
        decl.add("target", new STTargetWrapper(target));
        decl.add("converter", this);
        decl.add("services", new STServicesWrapper(menuProviders, null, resource));
        decl.add("resource", new StResourceWrapper(source, resource, favoritesService));

        List<FormField> fields = null;

        if (source instanceof List) {
            decl.add("source", new STListSourceWrapper((List<Object>) source));
            Object entity;
            try {
                Class<?> parameterType = resource.getParameterType();
                if (parameterType.equals(Map.class)) {
                    List<Map<String, Object>> currentEntity = (List<Map<String, Object>>) resource.getCurrentEntity();
                    if (currentEntity.size() > 0) {
                        fields = currentEntity.get(0).keySet().stream().map(key -> {
                            return new FormField(key, currentEntity.get(0).get(key));
                        }).collect(Collectors.toList());
                    }
                } else {
                    entity = parameterType.newInstance();
                    if (entity instanceof DynamicEntity) {
                        Set<EntityDynaProperty> properties = ((DynamicEntity) entity).getProperties();
                        fields = properties.stream().map(p -> {
                            return new FormField((DynamicEntity) entity, p, resource);
                        }).collect(Collectors.toList());
                    } else {
                        fields = ReflectionUtils.getInheritedFields(resource.getParameterType()).stream()
                                .filter(f -> test(resource, f)).sorted((f1, f2) -> sort(resource, f1, f2))
                                .map(f -> new FormField(f, resource, source, entity))//
                                .collect(Collectors.toList());
                    }
                }
                decl.add("fields", new STFieldsWrapper(fields));
            } catch (InstantiationException | IllegalAccessException e) {
                log.error(e.getMessage(), e);
            }
        } else {
            decl.add("source", new STSourceWrapper(source));
            if (source != null && (source instanceof SkysailResponse)) {
                Object entity = ((SkysailResponse<?>) source).getEntity();
                if (entity instanceof DynamicEntity) {
                    DynaProperty[] dynaProperties = ((DynamicEntity) entity).getInstance().getDynaClass()
                            .getDynaProperties();
                    fields = Arrays.stream(dynaProperties).map(d -> {
                        return new FormField((DynamicEntity) entity, d, resource);
                    }).collect(Collectors.toList());
                } else {
                    fields = ReflectionUtils.getInheritedFields(entity.getClass()).stream()
                            .filter(f -> test(resource, f))
                            .map(f -> new FormField(f, resource, source, ((SkysailResponse<?>) source).getEntity()))
                            .collect(Collectors.toList());
                }
                decl.add("fields", new STFieldsWrapper(fields));
            } else if (source != null && (source instanceof HashMap)) {

            } else {

            }
        }
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

    private int sort(SkysailServerResource<?> resource, Field f1, Field f2) {
        List<String> fieldNames = resource.getFields();
        return fieldNames.indexOf(f1.getName()) - fieldNames.indexOf(f2.getName());
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

    private boolean test(SkysailServerResource<?> resource, Field field) {
        List<String> fieldNames = resource.getFields();
        if (isValidFieldAnnotation(resource, field, fieldNames)) {
            return true;
        }

        // PostView postViewAnnotation = field.getAnnotation(PostView.class);
        // if (postViewAnnotation != null) {
        // if (!(Visibility.SHOW.equals(postViewAnnotation.visibility()))) {
        // return true;
        // }
        // }
        return false;
    }

    private boolean isValidFieldAnnotation(SkysailServerResource<?> resource, Field field, List<String> fieldNames) {
        io.skysail.api.forms.Field fieldAnnotation = field.getAnnotation(io.skysail.api.forms.Field.class);
        Reference referenceAnnotation = field.getAnnotation(Reference.class);
        if (fieldAnnotation == null && referenceAnnotation == null) {
            return false;
        }
        if (!(fieldNames.contains(field.getName()))) {
            return false;
        }
        if (resource instanceof PostEntityServerResource<?>) {
            PostView postViewAnnotation = field.getAnnotation(PostView.class);
            if (postViewAnnotation != null) {
                if (Visibility.HIDE.equals((postViewAnnotation.visibility()))) {
                    return false;
                }
                if (Visibility.SHOW.equals(postViewAnnotation.visibility())) {
                    return true;
                }
                if (Visibility.SHOW_IF_NULL.equals(postViewAnnotation.visibility())) {
                    if (resource.getRequest().toString().contains("/" + field.getName() + ":null/")) {
                        return true;
                    }
                }
            }
        }
        ListView listViewAnnotation = field.getAnnotation(ListView.class);
        if (listViewAnnotation == null) {
            return true;
        }
        return !listViewAnnotation.hide();
        // return
        // (!(Arrays.asList(fieldAnnotation.listView()).contains(ListViewEnum.HIDE)));
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

}
