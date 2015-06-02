package io.skysail.server.model;

import io.skysail.api.favorites.FavoritesService;
import io.skysail.api.links.Link;
import io.skysail.server.restlet.resources.*;
import io.skysail.server.restlet.sourceconverter.ListSourceHtmlConverter;
import io.skysail.server.utils.*;

import java.lang.reflect.Field;
import java.util.*;
import java.util.stream.Collectors;

import lombok.*;
import lombok.extern.slf4j.Slf4j;

import org.restlet.data.Header;
import org.restlet.representation.Variant;
import org.restlet.util.Series;

import de.twenty11.skysail.server.core.FormField;
import de.twenty11.skysail.server.core.restlet.ResourceContextId;

/**
 * The model of the resource from which the representation is derived.
 * 
 * <p>
 * Contrary to (e.g.) a JSON representation of an entity, an HTML representation... TODO  
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

    public ResourceModel(R resource, Object source) {
        this.resource = resource;
        this.source = source;
        
        parameterType = resource.getParameterType();
        List<Field> fields = ReflectionUtils.getInheritedFields(resource.getParameterType());
        entityModel = new EntityModel(fields);
        
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

    public boolean isPostEntityServerResource() {
        return resource instanceof PostEntityServerResource;
    }

    public boolean isPutEntityServerResource() {
        return resource instanceof PutEntityServerResource;
    }


    
    private void determineFormfields(R resource, Object source) {
        FieldFactory fieldFactory = FieldsFactory.getFactory(source, resource);
        log.info("using factory '{}' for {}-Source: {}", new Object[] { fieldFactory.getClass().getSimpleName(),
                source.getClass().getSimpleName(), source });
        try {
            formfields = fieldFactory.determineFrom(resource);
        } catch (Exception e) {
            log.error(e.getMessage(),e);
        }
    }

    public void convert(Variant target) {
        if (source instanceof List) {
            this.convertedSource = new ListSourceHtmlConverter(source, target).convert((ResourceModel<SkysailServerResource<?>,?>)this);
        } else {
            this.convertedSource = source;
        }
    }

    public void setFavoritesService(FavoritesService favoritesService) {
        this.favoritesService = favoritesService;
    }

    
   

}
