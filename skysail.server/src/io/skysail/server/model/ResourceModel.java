package io.skysail.server.model;

import io.skysail.api.favorites.FavoritesService;
import io.skysail.api.links.Link;
import io.skysail.api.responses.*;
import io.skysail.server.restlet.resources.*;
import io.skysail.server.restlet.sourceconverter.ListSourceHtmlConverter;
import io.skysail.server.utils.*;

import java.util.*;
import java.util.stream.Collectors;

import lombok.*;
import lombok.extern.slf4j.Slf4j;

import org.restlet.data.*;
import org.restlet.representation.Variant;
import org.restlet.util.Series;

import de.twenty11.skysail.server.core.FormField;
import de.twenty11.skysail.server.core.restlet.ResourceContextId;
import de.twenty11.skysail.server.services.MenuItemProvider;

/**
 * The model of the resource from which the representation is derived.
 *
 * <p>
 * The typical setup for a skysail client is something like single-page application, i.e
 * the client initiates all the request to the resources it needs and builds up the GUI 
 * from that. Subsequent requests might alter only parts of the GUI.
 * </p>
 * 
 * <p>
 * The purpose of this class is a little bit different, as it aims to be more generic. All 
 * relevant data (links, the current user, pagination information, the entities fields and their 
 * metadata and so on) can be accessed, so that a complete "one-time-request" representation
 * of the current resource can be generated from this information.
 * </p>
 *
 * @param <R>
 * @param <T>
 */
@Slf4j
@Getter
@ToString
public class ResourceModel<R extends SkysailServerResource<T>, T> {

    private R resource;
    private Class<?> parameterType;
    private EntityModel entityModel;
    private String title = "Skysail";
    private Object source;
    private List<FormField> formfields;
    private Object convertedSource;
    private FavoritesService favoritesService;

    @Getter
    private STTargetWrapper target;

    @Getter
    private STServicesWrapper services;

    public ResourceModel(R resource, Object source, Variant target) {
        this.resource = resource;
        this.source = source;
        this.target = new STTargetWrapper(target);
        
        parameterType = resource.getParameterType();
        determineFormfields(resource, source);
    }

    public Map<String, Object> dataFromMap(Map<String, Object> props) {
        return entityModel.dataFromMap(props , resource);
    }
    
    public List<Breadcrumb> getBreadcrumbs() {
        return new Breadcrumbs(favoritesService).create(resource);
    }

    public List<RepresentationLink> getRepresentations() {
        Set<String> supportedMediaTypes = ResourceUtils.getSupportedMediaTypes(resource, resource.getCurrentEntity());
        return supportedMediaTypes.stream().map(mediaType -> {
            return new RepresentationLink(mediaType, resource.getCurrentEntity());
        }).collect(Collectors.toList());
    }

    public List<Link> getLinks() throws Exception {
        return resource.getAuthorizedLinks();
    }    
    
    public String getAppNavTitle() {
        return resource.getFromContext(ResourceContextId.APPLICATION_NAVIGATION_TITLE);
    }
    
    public String getPagination() {
        Series<Header> headers = HeadersUtils.getHeaders(resource.getResponse());
        String pagesAsString = headers.getFirstValue(HeadersUtils.PAGINATION_PAGES);
        if (pagesAsString == null || pagesAsString.trim().length() == 0) {
            return "";
        }
        int pages = Integer.parseInt(pagesAsString);
        if (pages == 1) {
            return "";
        }
        int page = 1;
        String pageAsString = headers.getFirstValue(HeadersUtils.PAGINATION_PAGE);
        if (pageAsString != null) {
            page = Integer.parseInt(pageAsString);
        }

        StringBuilder sb = new StringBuilder();
        sb.append("<nav>");
        sb.append("<ul class='pagination'>");
        String cssClass = (1 == page) ? "class='disabled'" : "";

        sb.append("<li " + cssClass + "><a href='?").append(SkysailServerResource.PAGE_PARAM_NAME).append("=" + (page - 1)
                + "'><span aria-hidden='true'>&laquo;</span><span class='sr-only'>Previous</span></a></li>");
        for (int i = 1; i <= pages; i++) {
            cssClass = (i == page) ? " class='active'" : "";
            sb.append("<li" + cssClass + "><a href='?").append(SkysailServerResource.PAGE_PARAM_NAME).append("=" + i + "'>" + i + "</a></li>");
        }
        cssClass = (pages == page) ? " class='disabled'" : "";
        sb.append("<li" + cssClass + "><a href='?").append(SkysailServerResource.PAGE_PARAM_NAME).append("=" + (page + 1)
                + "'><span aria-hidden='true'>&raquo;</span><span class='sr-only'>Next</span></a></li>");
        sb.append("</ul>");
        sb.append("</nav>");
        return sb.toString();
    }
    
    public String getStatus() {
        Status status = resource.getStatus();
        StringBuilder sb = new StringBuilder();
        String panelClass = "panel-success";
        if (String.valueOf(status.getCode()).startsWith("4")) {
            panelClass = "panel-warning";
        }
        if (String.valueOf(status.getCode()).startsWith("5")) {
            panelClass = "panel-danger";
        }
        sb.append("<div class='panel ").append(panelClass).append("'>");
        sb.append("  <div class='panel-heading' role='tab' id='headingStatus'>");
        sb.append("    <h4 class='panel-title'>");
        sb.append("      <a data-toggle='collapse' data-parent='#accordion' href='#collapseStatus' aria-expanded='false' aria-controls='collapseStatus'>");
        sb.append("        Http Status Code: ").append(status.getCode());
        sb.append("      </a>");
        sb.append("    </h4>");
        sb.append("  </div>");
        sb.append("  <div id='collapseStatus' class='panel-collapse collapse in' role='tabpanel' aria-labelledby='headingStatus'>");
        sb.append("    <div class='panel-body'>").append(status.getDescription()).append("</div>");
        sb.append("  </div>");
        sb.append("</div>");
        return sb.toString();
    }
    
    public String getClassname() {
        return resource.getClass().getName();
    }

    public String getEntityType() {
        String result = resource.getEntityType().replace("<", "&lt;").replace(">", "&gt;");
        if (!result.contains("skysail")) {
            return result;
        }
        // https://github.com/evandor/skysail/blob/master/skysail.server.app.i18n/src/de/twenty11/skysail/server/app/i18n/messages/BundleMessages.java
        String bundleName = resource.getApplication().getBundle().getSymbolicName();
        StringBuilder sb = new StringBuilder("<a target='_blank' href='https://github.com/evandor/skysail/blob/master/")
                //
                .append(bundleName).append("/").append("src/")
                //
                .append(resource.getEntityType().replace("List of ", "").replace(".", "/")).append(".java")
                .append("'>View on github</a>");
        return result + "&nbsp;" + sb.toString();
    }

    public boolean isForm() {
        if (source instanceof FormResponse) {
            return ((SkysailResponse<?>) source).isForm();
        }
        if (source instanceof ConstraintViolationsResponse) {
            return ((ConstraintViolationsResponse<?>) source).isForm();
        }
        return false;
    }
    
    public boolean isList() {
        return source instanceof List;
    }



    public boolean isPostEntityServerResource() {
        return resource instanceof PostEntityServerResource;
    }

    public boolean isPutEntityServerResource() {
        return resource instanceof PutEntityServerResource;
    }

    public boolean isSubmitButtonNeeded() {
        return entityModel.isSubmitButtonNeeded();
    }

    
    private void determineFormfields(R resource, Object source) {
        FieldFactory fieldFactory = FieldsFactory.getFactory(source, resource);
        log.debug("using factory '{}' for {}-Source: {}", new Object[] { fieldFactory.getClass().getSimpleName(),
                source.getClass().getSimpleName(), source });
        try {
            formfields = fieldFactory.determineFrom(resource);
            entityModel = new EntityModel(formfields);
        } catch (Exception e) {
            log.error(e.getMessage(),e);
        }
    }

    public void convert() {
        if (source instanceof List) {
            Variant variant = target.getTarget();
            this.convertedSource = new ListSourceHtmlConverter(source, variant).convert((ResourceModel<SkysailServerResource<?>,?>)this);
        } else {
            this.convertedSource = source;
        }
    }

    public void setFavoritesService(FavoritesService favoritesService) {
        this.favoritesService = favoritesService;
    }

    public void setMenuItemProviders(Set<MenuItemProvider> menuProviders) {
        this.services = new STServicesWrapper(menuProviders, null, resource);
    }

    
    public void addAssociatedLinks() {
        if (!(getResource() instanceof ListServerResource)) {
            return;
        }
        ListServerResource<?> listServerResource = (ListServerResource<?>) getResource();
        List<Link> links = listServerResource.getLinks();
        List<Class<? extends SkysailServerResource<?>>> entityResourceClass = listServerResource
                .getAssociatedServerResources();
        if (entityResourceClass != null && convertedSource instanceof List) {
            List<?> sourceAsList = (List<?>) getConvertedSource();
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


}
