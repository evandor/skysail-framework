package io.skysail.server.converter.impl;

import io.skysail.server.converter.HtmlConverter;
import io.skysail.server.converter.Notification;
import io.skysail.server.converter.stringtemplate.STGroupBundleDir;
import io.skysail.server.converter.wrapper.STFieldsWrapper;
import io.skysail.server.converter.wrapper.STListSourceWrapper;
import io.skysail.server.converter.wrapper.STResourceWrapper;
import io.skysail.server.converter.wrapper.STServicesWrapper;
import io.skysail.server.converter.wrapper.STSourceWrapper;
import io.skysail.server.converter.wrapper.STTargetWrapper;

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

import de.twenty11.skysail.api.forms.ListView;
import de.twenty11.skysail.api.forms.Reference;
import de.twenty11.skysail.api.responses.LinkHeaderRelation;
import de.twenty11.skysail.api.responses.Linkheader;
import de.twenty11.skysail.api.responses.LinkheaderRole;
import de.twenty11.skysail.api.responses.SkysailResponse;
import de.twenty11.skysail.server.app.SkysailApplication;
import de.twenty11.skysail.server.app.SourceWrapper;
import de.twenty11.skysail.server.beans.DynamicEntity;
import de.twenty11.skysail.server.core.FormField;
import de.twenty11.skysail.server.core.restlet.EntityServerResource;
import de.twenty11.skysail.server.core.restlet.ListServerResource;
import de.twenty11.skysail.server.core.restlet.SkysailServerResource;
import de.twenty11.skysail.server.core.restlet.utils.CookiesUtils;
import de.twenty11.skysail.server.core.restlet.utils.StringParserUtils;
import de.twenty11.skysail.server.services.MenuItemProvider;
import de.twenty11.skysail.server.services.UserManager;
import de.twenty11.skysail.server.utils.ReflectionUtils;
import de.twenty11.skysail.server.utils.ResourceUtils;

@Slf4j
public class StringTemplateRenderer {

    private STGroupBundleDir importedGroupBundleDir;
    private Set<MenuItemProvider> menuProviders;
    private UserManager userManager;
    private String templateFromCookie;
    private HtmlConverter htmlConverter;

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
        addSubstitutions(sourceWrapper.getConvertedSource(), resource, index, target, menuProviders, userManager);
        checkForInspection(resource, index);

        return createRepresentation(index, stGroup);
    }

    private STGroupBundleDir createSringTemplateGroup(Resource resource, String mediaType) {
        SkysailApplication currentApplication = (SkysailApplication) resource.getApplication();
        Bundle appBundle = currentApplication.getBundle();
        String resourcePath = ("/templates/" + mediaType).replace("/*", "");
        log.info("reading templates from resource path '{}'", resourcePath);
        URL templatesResource = appBundle.getResource("/templates");// resourcePath);
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
        Class<? extends EntityServerResource<?>> entityResourceClass = listServerResource.getAssociatedEntityResource();
        if (entityResourceClass != null && sourceWrapper.getConvertedSource() instanceof List) {
            EntityServerResource<?> esr = ResourceUtils.createEntityServerResource(entityResourceClass, resource);
            List<Linkheader> linkheader = esr.getLinkheaderAuthorized();

            List<?> sourceAsList = (List<?>) sourceWrapper.getConvertedSource();
            for (Object object : sourceAsList) {
                if (!(object instanceof Map)) {
                    continue;
                }
                StringBuilder sb = new StringBuilder();

                String id = guessId(object);
                linkheader.stream().filter(lh -> {
                    return lh.getRole().equals(LinkheaderRole.DEFAULT);
                }).forEach(link -> addLinkHeader(link, esr, id, listServerResource, sb));
                ((Map<String, Object>) object).put("_links", sb.toString());
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
        Map<String, String> entity = ((Map<String, String>) object);

        if (entity.get("id") != null) {
            Object value = entity.get("id");
            return value.toString().replace("#", "");
        } else if (entity.get("@rid") != null) {
            return (entity.get("@rid").replace("#", ""));
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
            Set<MenuItemProvider> menuProviders, UserManager userManager) {

        decl.add("user", SecurityUtils.getSubject());
        decl.add("target", new STTargetWrapper(target));
        decl.add("converter", this);
        decl.add("services", new STServicesWrapper(menuProviders, null, resource));
        decl.add("resource", new STResourceWrapper(source, resource));

        List<FormField> fields = null;

        if (source instanceof List) {
            decl.add("source", new STListSourceWrapper((List<Object>) source));
            Object entity;
            try {
                entity = resource.getParameterType().newInstance();
                fields = ReflectionUtils.getInheritedFields(resource.getParameterType()).stream()
                        .filter(f -> test(resource, f)).sorted((f1, f2) -> sort(resource, f1, f2))
                        .map(f -> new FormField(f, resource, userManager, source, entity))//
                        .collect(Collectors.toList());

                decl.add("fields", new STFieldsWrapper(fields));
            } catch (InstantiationException | IllegalAccessException e) {
                e.printStackTrace();
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
                    fields = ReflectionUtils
                            .getInheritedFields(entity.getClass())
                            .stream()
                            .filter(f -> test(resource, f))
                            .map(f -> new FormField(f, resource, userManager, source, ((SkysailResponse<?>) source)
                                    .getEntity())).collect(Collectors.toList());
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

    private void addLinkHeader(Linkheader link, Resource entityResource, String id, ListServerResource<?> resource,
            StringBuilder sb) {
        String path = link.getUri();
        String href = StringParserUtils.substitutePlaceholders(path, entityResource);

        // hmmm... last resort
        if (id != null && href.contains("{") && href.contains("}")) {
            // path = path.replace("{id}", id);
            href = href.replaceFirst(StringParserUtils.placeholderPattern.toString(), id);
        }

        sb.append("<a class='btn btn-mini' href='").append(href).append("'>").append(link.getTitle())
                .append("</a>&nbsp;");

        resource.getLinkheader().add(
                new Linkheader.Builder(href).relation(LinkHeaderRelation.ITEM)
                        .title("item " + id == null ? "unknown" : id).build());

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
        de.twenty11.skysail.api.forms.Field fieldAnnotation = field
                .getAnnotation(de.twenty11.skysail.api.forms.Field.class);
        boolean isValidFieldAnnotation = (fieldAnnotation != null
                && (!(fieldAnnotation.listView().equals(ListView.HIDE))) && fieldNames.contains(field.getName()));
        if (isValidFieldAnnotation) {
            return true;
        }

        Reference referenceAnnotation = field.getAnnotation(Reference.class);
        return (referenceAnnotation != null);
    }

    public void setMenuProviders(Set<MenuItemProvider> menuProviders) {
        this.menuProviders = menuProviders;

    }

    public void setUserManager(UserManager userManager) {
        this.userManager = userManager;
    }

    private void checkForInspection(Resource resource, ST index) {
        String inspect = CookiesUtils.getInpsectFromCookie(resource.getRequest());
        if (resource.getHostRef().getHostDomain().contains("localhost") && inspect != null
                && inspect.equalsIgnoreCase("on")) {
            index.inspect();
        }
    }

}
