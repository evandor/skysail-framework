package io.skysail.server.converter.impl;

import io.skysail.api.favorites.FavoritesService;
import io.skysail.api.links.Link;
import io.skysail.api.peers.PeersProvider;
import io.skysail.server.app.SkysailApplication;
import io.skysail.server.converter.*;
import io.skysail.server.converter.stringtemplate.STGroupBundleDir;
import io.skysail.server.converter.wrapper.*;
import io.skysail.server.model.ResourceModel;
import io.skysail.server.restlet.SourceWrapper;
import io.skysail.server.restlet.resources.*;

import java.net.URL;
import java.util.*;
import java.util.stream.Collectors;

import lombok.extern.slf4j.Slf4j;

import org.apache.shiro.SecurityUtils;
import org.osgi.framework.Bundle;
import org.restlet.Request;
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

    public StringRepresentation createRepresenation(Object source, Variant target,
            SkysailServerResource<?> resource) {
        
        @SuppressWarnings({ "rawtypes", "unchecked" })
        ResourceModel<SkysailServerResource<?>,?> resourceModel = new ResourceModel(resource);

        templateFromCookie = CookiesUtils.getTemplateFromCookie(resource.getRequest());

        SourceWrapper sourceWrapper = new SourceWrapper(source, target, resourceModel);
        
        STGroupBundleDir stGroup = createSringTemplateGroup(resource, target.getMediaType().getName());
        
        ST index = getStringTemplateIndex(resource, stGroup);
        
        addAssociatedLinks(resource, sourceWrapper);
        addSubstitutions(sourceWrapper.getConvertedSource(), resourceModel, index, target, menuProviders);
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

        if (isMobile(resource.getRequest())) {
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

    private void addAssociatedLinks(Resource resource, SourceWrapper sourceWrapper) {
        if (!(resource instanceof ListServerResource)) {
            return;
        }
        ListServerResource<?> listServerResource = (ListServerResource<?>) resource;
        List<Link> links = listServerResource.getLinks();
        List<Class<? extends SkysailServerResource<?>>> entityResourceClass = listServerResource
                .getAssociatedServerResources();
        if (entityResourceClass != null && sourceWrapper.getConvertedSource() instanceof List) {
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
    private void addSubstitutions(Object source, ResourceModel<SkysailServerResource<?>,?> resourceModel, ST decl, Variant target,
            Set<MenuItemProvider> menuProviders) {

        SkysailServerResource<?> resource = resourceModel.getResource();
        
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
    
    private boolean isMobile(Request request) {
        String userAgent = request.getHeaders().getFirst("User-agent", true).toString();
        // http://detectmobilebrowsers.com/
        if(userAgent.toLowerCase().matches("(?i).*((android|bb\\d+|meego).+mobile|avantgo|bada\\/|blackberry|blazer|compal|elaine|fennec|hiptop|iemobile|ip(hone|od)|iris|kindle|lge |maemo|midp|mmp|mobile.+firefox|netfront|opera m(ob|in)i|palm( os)?|phone|p(ixi|re)\\/|plucker|pocket|psp|series(4|6)0|symbian|treo|up\\.(browser|link)|vodafone|wap|windows ce|xda|xiino).*")
                ||
          userAgent.substring(0,4).matches("(?i)1207|6310|6590|3gso|4thp|50[1-6]i|770s|802s|a wa|abac|ac(er|oo|s\\-)|ai(ko|rn)|al(av|ca|co)|amoi|an(ex|ny|yw)|aptu|ar(ch|go)|as(te|us)|attw|au(di|\\-m|r |s )|avan|be(ck|ll|nq)|bi(lb|rd)|bl(ac|az)|br(e|v)w|bumb|bw\\-(n|u)|c55\\/|capi|ccwa|cdm\\-|cell|chtm|cldc|cmd\\-|co(mp|nd)|craw|da(it|ll|ng)|dbte|dc\\-s|devi|dica|dmob|do(c|p)o|ds(12|\\-d)|el(49|ai)|em(l2|ul)|er(ic|k0)|esl8|ez([4-7]0|os|wa|ze)|fetc|fly(\\-|_)|g1 u|g560|gene|gf\\-5|g\\-mo|go(\\.w|od)|gr(ad|un)|haie|hcit|hd\\-(m|p|t)|hei\\-|hi(pt|ta)|hp( i|ip)|hs\\-c|ht(c(\\-| |_|a|g|p|s|t)|tp)|hu(aw|tc)|i\\-(20|go|ma)|i230|iac( |\\-|\\/)|ibro|idea|ig01|ikom|im1k|inno|ipaq|iris|ja(t|v)a|jbro|jemu|jigs|kddi|keji|kgt( |\\/)|klon|kpt |kwc\\-|kyo(c|k)|le(no|xi)|lg( g|\\/(k|l|u)|50|54|\\-[a-w])|libw|lynx|m1\\-w|m3ga|m50\\/|ma(te|ui|xo)|mc(01|21|ca)|m\\-cr|me(rc|ri)|mi(o8|oa|ts)|mmef|mo(01|02|bi|de|do|t(\\-| |o|v)|zz)|mt(50|p1|v )|mwbp|mywa|n10[0-2]|n20[2-3]|n30(0|2)|n50(0|2|5)|n7(0(0|1)|10)|ne((c|m)\\-|on|tf|wf|wg|wt)|nok(6|i)|nzph|o2im|op(ti|wv)|oran|owg1|p800|pan(a|d|t)|pdxg|pg(13|\\-([1-8]|c))|phil|pire|pl(ay|uc)|pn\\-2|po(ck|rt|se)|prox|psio|pt\\-g|qa\\-a|qc(07|12|21|32|60|\\-[2-7]|i\\-)|qtek|r380|r600|raks|rim9|ro(ve|zo)|s55\\/|sa(ge|ma|mm|ms|ny|va)|sc(01|h\\-|oo|p\\-)|sdk\\/|se(c(\\-|0|1)|47|mc|nd|ri)|sgh\\-|shar|sie(\\-|m)|sk\\-0|sl(45|id)|sm(al|ar|b3|it|t5)|so(ft|ny)|sp(01|h\\-|v\\-|v )|sy(01|mb)|t2(18|50)|t6(00|10|18)|ta(gt|lk)|tcl\\-|tdg\\-|tel(i|m)|tim\\-|t\\-mo|to(pl|sh)|ts(70|m\\-|m3|m5)|tx\\-9|up(\\.b|g1|si)|utst|v400|v750|veri|vi(rg|te)|vk(40|5[0-3]|\\-v)|vm40|voda|vulc|vx(52|53|60|61|70|80|81|83|85|98)|w3c(\\-| )|webc|whit|wi(g |nc|nw)|wmlb|wonu|x700|yas\\-|your|zeto|zte\\-")) {
          return true;
        }
        return false;
    }



}
