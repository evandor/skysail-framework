package io.skysail.server.model;

import io.skysail.api.domain.Identifiable;
import io.skysail.api.favorites.FavoritesService;
import io.skysail.api.links.Link;
import io.skysail.api.responses.*;
import io.skysail.server.restlet.resources.*;
import io.skysail.server.utils.*;

import java.text.DateFormat;
import java.util.*;
import java.util.stream.Collectors;

import lombok.*;
import lombok.extern.slf4j.Slf4j;

import org.apache.commons.lang.StringUtils;
import org.restlet.data.*;
import org.restlet.engine.resource.VariantInfo;
import org.restlet.representation.Variant;
import org.restlet.util.Series;

import com.fasterxml.jackson.databind.*;

import de.twenty11.skysail.server.core.FormField;
import de.twenty11.skysail.server.core.restlet.ResourceContextId;
import de.twenty11.skysail.server.services.MenuItemProvider;

/**
 * The model of the resource from which the html representation is derived.
 *
 * <p>
 * The typical setup for a skysail client is something like single-page
 * application, i.e the client initiates all the request to the resources it
 * needs and builds up the GUI from that. Subsequent requests might alter only
 * parts of the GUI.
 * </p>
 * 
 * <p>
 * The purpose of this class is a little bit different, as it aims to be more
 * generic. All relevant rawData (links, the current user, pagination
 * information, the entities fields, their metadata and associated entities and
 * so on) can be accessed, so that a complete "one-time-request" representation
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

    private volatile ObjectMapper mapper = new ObjectMapper();

    private final R resource;
    private final SkysailResponse<?> response;
    private final STTargetWrapper target;
    private final Class<?> parameterizedType;

    private EntityModel<R> rootEntity;
    
    private Map<String, FormField> fields;

    private List<Map<String, Object>> rawData; // raw, original rawData, always as List, even for only one entry.
    private List<Map<String, Object>> data; // converted data (truncated, augemented, formatted, translated ...)
    
    private String title = "Skysail";
    private FavoritesService favoritesService;
    private STServicesWrapper services;

    public ResourceModel(R resource, SkysailResponse<?> response) {
        this(resource, response, new VariantInfo(MediaType.TEXT_HTML));
    }
    
    public ResourceModel(R resource, SkysailResponse<?> source, Variant target) {

        mapper.configure(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS, false);
        mapper.configure(SerializationFeature.FAIL_ON_EMPTY_BEANS, false);
        mapper.setDateFormat(DateFormat.getDateInstance(DateFormat.LONG, determineLocale(resource)));

        rawData = getData(source);

        this.resource = resource;
        this.response = source;
        this.target = new STTargetWrapper(target);

        parameterizedType = resource.getParameterizedType();

        determineFormfields();
        
        rootEntity = new EntityModel<R>(response.getEntity(), resource);

        data = convert();

        addAssociatedLinks();
    }

    @SuppressWarnings("unchecked")
    private List<Map<String, Object>> getData(Object source) {
        List<Map<String, Object>> result = new ArrayList<>();
        if (source instanceof ListServerResponse) {
            for (Object object : ((ListServerResponse<?>) source).getEntity()) {
                result.add((Map<String, Object>) mapper.convertValue(object, LinkedHashMap.class));
            }
        } else if (source instanceof EntityServerResponse) {
            result.add((Map<String, Object>) mapper.convertValue(((EntityServerResponse<?>) source).getEntity(),
                    LinkedHashMap.class));

        } else if (source instanceof FormResponse) {
            result.add((Map<String, Object>) mapper.convertValue(((FormResponse<?>) source).getEntity(), LinkedHashMap.class));
        }
        return result;
    }

    private void determineFormfields() {
        FieldFactory fieldFactory = FieldsFactory.getFactory(response.getEntity(), resource);
        try {
            fields = fieldFactory.determineFrom(resource);
        } catch (Exception e) {
            log.error(e.getMessage(), e);
        }
    }

    private List<Map<String, Object>> convert() {
        List<Map<String, Object>> result = new ArrayList<>();
        rawData.stream().forEach(row -> {
            Map<String, Object> newRow = new HashMap<>();
            result.add(newRow);
            row.keySet().stream().forEach(columnName -> {
                apply(newRow, row, columnName);
            });
        });
        return result;
    }

    private void apply(Map<String, Object> newRow, Map<String, Object> dataRow, String columnName) {
        FormField formField = fields.get(columnName);
        if (formField != null) {
            newRow.put(columnName, formField.process(response, dataRow, columnName));
        } else {
            newRow.put(columnName, dataRow.get(columnName));
        }
    }

    public List<Breadcrumb> getBreadcrumbs() {
        return new Breadcrumbs(favoritesService).create(resource);
    }

    public List<RepresentationLink> getRepresentations() {
        Set<String> supportedMediaTypes = ResourceUtils.getSupportedMediaTypes(resource, getParameterizedType());
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

        sb.append("<li " + cssClass + "><a href='?")
                .append(SkysailServerResource.PAGE_PARAM_NAME)
                .append("=" + (page - 1)
                        + "'><span aria-hidden='true'>&laquo;</span><span class='sr-only'>Previous</span></a></li>");
        for (int i = 1; i <= pages; i++) {
            cssClass = (i == page) ? " class='active'" : "";
            sb.append("<li" + cssClass + "><a href='?").append(SkysailServerResource.PAGE_PARAM_NAME)
                    .append("=" + i + "'>" + i + "</a></li>");
        }
        cssClass = (pages == page) ? " class='disabled'" : "";
        sb.append("<li" + cssClass + "><a href='?")
                .append(SkysailServerResource.PAGE_PARAM_NAME)
                .append("=" + (page + 1)
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
        sb.append("      <a rawData-toggle='collapse' rawData-parent='#accordion' href='#collapseStatus' aria-expanded='false' aria-controls='collapseStatus'>");
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
        return response.isForm();
    }

    public boolean isList() {
        return response.getEntity() instanceof List;
    }

    public boolean isPostEntityServerResource() {
        return resource instanceof PostEntityServerResource;
    }

    public boolean isPutEntityServerResource() {
        return resource instanceof PutEntityServerResource;
    }

    public boolean isSubmitButtonNeeded() {
        return !fields.values().stream().filter(f -> {
            return f.isSubmitField();
        }).findFirst().isPresent();
    }

    public void setFavoritesService(FavoritesService favoritesService) {
        this.favoritesService = favoritesService;
    }

    public void setMenuItemProviders(Set<MenuItemProvider> menuProviders) {
        this.services = new STServicesWrapper(menuProviders, null, resource);
    }

    private void addAssociatedLinks() {
        if (!(getResource() instanceof ListServerResource)) {
            return;
        }
        ListServerResource<?> listServerResource = (ListServerResource<?>) getResource();
        List<Link> links = listServerResource.getLinks();
        List<Class<? extends SkysailServerResource<?>>> entityResourceClass = listServerResource
                .getAssociatedServerResources();
        if (entityResourceClass != null) {
            List<Map<String, Object>> sourceAsList = data;
            for (Map<String, Object> dataRow : sourceAsList) {
                String id = guessId(dataRow);
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

                dataRow.put("_links", linkshtml);
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

    protected Locale determineLocale(SkysailServerResource<?> resource) {
        if (resource.getRequest() == null || resource.getRequest().getClientInfo() == null) {
            return Locale.getDefault();
        }
        List<Preference<Language>> acceptedLanguages = resource.getRequest().getClientInfo().getAcceptedLanguages();
        Locale localeToUse = Locale.getDefault();
        if (!acceptedLanguages.isEmpty()) {
            String[] languageSplit = acceptedLanguages.get(0).getMetadata().getName().split("-");
            if (languageSplit.length == 1) {
                localeToUse = new Locale(languageSplit[0]);
            } else if (languageSplit.length == 2) {
                localeToUse = new Locale(languageSplit[0], languageSplit[1]);
            }
        }
        return localeToUse;
    }

    public List<FormField> getFormfields() {
        return new ArrayList<FormField>(fields.values());
    }
    
    public boolean isConstraintViolationsResponse() {
        return response instanceof ConstraintViolationsResponse;
    }
    
    public String getClasslevelViolationMessage() {
        if (!isConstraintViolationsResponse()) {
            return null;
        }
        Set<ConstraintViolationDetails> violations = ((ConstraintViolationsResponse<?>)response).getViolations();
        String msg = violations.stream().filter(v -> {
            return v.getPropertyPath().equals("");
        }).map(v -> {
            return v.getMessage();
        }).collect(Collectors.joining(", "));
        return StringUtils.isEmpty(msg) ? null : msg;
    }
    
    

    public String getFormTarget() {
        if (!(response instanceof FormResponse)) {
            return null;
        }
        return ((FormResponse<?>) response).getTarget();
    }

    public String getDeleteFormTarget() {
        if (!(response instanceof FormResponse)) {
            return null;
        }
        FormResponse<?> formResponse = (FormResponse<?>) response;
        Object entity = formResponse.getEntity();
        if (entity instanceof Identifiable) {
            return "../" + ((Identifiable)entity).getId().replace("#", "");
        }
        return "../" + ((FormResponse<?>) response).getId();
    }

    public String getRedirectBackTo() {
        if (response instanceof FormResponse) {
            return ((FormResponse<?>) response).getRedirectBackTo();
        }
        return null;
    }

}
