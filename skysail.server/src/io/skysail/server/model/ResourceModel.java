package io.skysail.server.model;

import java.text.*;
import java.util.*;
import java.util.stream.Collectors;

import org.apache.commons.beanutils.*;
import org.apache.commons.lang.StringUtils;
import org.restlet.data.*;
import org.restlet.engine.resource.VariantInfo;
import org.restlet.representation.Variant;
import org.restlet.util.Series;

import com.fasterxml.jackson.databind.*;

import io.skysail.api.links.LinkRelation;
import io.skysail.api.responses.*;
import io.skysail.api.search.*;
import io.skysail.domain.Identifiable;
import io.skysail.domain.core.*;
import io.skysail.server.ResourceContextId;
import io.skysail.server.app.SkysailApplication;
import io.skysail.server.domain.jvm.*;
import io.skysail.server.features.GuiFeatures;
import io.skysail.server.forms.*;
import io.skysail.server.forms.helper.CellRendererHelper;
import io.skysail.server.menus.MenuItemProvider;
import io.skysail.server.rendering.Theme;
import io.skysail.server.restlet.resources.*;
import io.skysail.server.utils.*;
import lombok.*;

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

    private List<Map<String, Object>> rawData; // raw, original rawData, always
                                               // as List, even for only one
                                               // entry.
    private List<Map<String, Object>> data; // converted data (truncated,
                                            // augemented, formatted, translated
                                            // ...)

    private String title = "Skysail";
    private STServicesWrapper services;
    private DateFormat dateFormat;
    private Theme theme;
    private SearchService searchService;
    private Map<String, FormField> dynaFields = new HashMap<>();

    public ResourceModel(R resource, SkysailResponse<?> response) {
        this(resource, response, new VariantInfo(MediaType.TEXT_HTML), new Theme());
    }

    public ResourceModel(R resource, SkysailResponse<?> skysailResponse, Variant target, Theme theme) {

        this.theme = theme;
        Locale locale = ResourceUtils.determineLocale(resource);
        dateFormat = DateFormat.getDateTimeInstance(DateFormat.MEDIUM, DateFormat.SHORT, locale);

        mapper.configure(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS, true);
        mapper.configure(SerializationFeature.FAIL_ON_EMPTY_BEANS, false);

        rawData = getData(skysailResponse, resource);

        this.resource = resource;
        this.response = skysailResponse;
        this.target = new STTargetWrapper(target);

        parameterizedType = resource.getParameterizedType();

        fields = FormfieldUtils.determineFormfields(response, resource);
        if (fields.size() == 0) {
            fields = dynaFields;
        }

        rootEntity = new EntityModel<>(response.getEntity(), resource);

        String identifierName = getIdentifierFormField(rawData);
        data = convert(identifierName, resource);

        addAssociatedLinks(data);
        addAssociatedLinks(rawData);
    }

    @SuppressWarnings("unchecked")
    private List<Map<String, Object>> getData(Object source, R theResource) {
        List<Map<String, Object>> result = new ArrayList<>();
        if (source instanceof ListServerResponse) {
            List<?> list = ((ListServerResponse<?>) source).getEntity();
            if (list != null) {
                for (Object object : list) {
                    if (object instanceof Document) {
                        result.add(((Document) object).getDocMap());
                    } else if (object instanceof DynamicBean) {
                        DynamicBean dynaClass = (DynamicBean) object;
                        DynaBean bean = dynaClass.getBean();
                        Map<String, Object> map = new HashMap<>();
                        for (DynaProperty prop : dynaClass.getDynaProperties()) {
                            map.put(prop.getName(), bean.get(prop.getName()));
                            if (dynaFields.get(prop.getName()) == null) {
                                dynaFields.put(prop.getName(),
                                        new FormField(new FieldModel(prop.getName(), String.class), theResource));
                            }
                        }
                        result.add(map);
                    } else {
                        result.add((Map<String, Object>) mapper.convertValue(object, LinkedHashMap.class));
                    }
                }
            }
        } else if (source instanceof RelationTargetResponse) {
            List<?> list = ((RelationTargetResponse<?>) source).getEntity();
            if (list != null) {
                for (Object object : list) {
                    if (object instanceof Document) {
                        throw new UnsupportedOperationException();
                    } else if (object instanceof DynamicBean) {
                        throw new UnsupportedOperationException();
                    } else {
                        result.add((Map<String, Object>) mapper.convertValue(object, LinkedHashMap.class));
                    }
                }
            }
        } else if (source instanceof EntityServerResponse) {
            result.add((Map<String, Object>) mapper.convertValue(((EntityServerResponse<?>) source).getEntity(),
                    LinkedHashMap.class));

        } else if (source instanceof FormResponse) {
            //Object deserializableObject = AnnotationUtils.removeRelationData();
            result.add((Map<String, Object>) mapper.convertValue(((FormResponse<?>) source).getEntity(), LinkedHashMap.class));
        } else if (source instanceof ConstraintViolationsResponse) {
            Object entity = ((ConstraintViolationsResponse<?>) source).getEntity();
            result.add((Map<String, Object>) mapper.convertValue(entity, LinkedHashMap.class));
        }
        return result;
    }

    private String getIdentifierFormField(@NonNull List<Map<String, Object>> theData) {
        return "id"; // for now
    }

    private List<Map<String, Object>> convert(String identifierName, R r) {
        List<Map<String, Object>> result = new ArrayList<>();
        rawData.stream().filter(row -> {
            return row != null;
        }).forEach(row -> {
            Map<String, Object> newRow = new HashMap<>();
            result.add(newRow);
            row.keySet().stream().forEach(columnName -> {
                Object identifier = row.get(identifierName);
                apply(newRow, row, columnName, identifier, r);
            });
        });
        return result;
    }

    private void apply(Map<String, Object> newRow, Map<String, Object> dataRow, String columnName, Object id, R r) {

        Optional<FieldModel> field = getDomainField(columnName);
        if (field.isPresent()) {
            newRow.put(columnName, calc((ClassFieldModel) field.get(), dataRow, columnName, id, r));
        } else if ("id".equals(columnName)) {
            newRow.put(columnName, dataRow.get("id"));// calc(formField, dataRow, columnName, id));
        } else { // deprecated old style
            //FormField formField = fields.get(columnName);
            //newRow.put(columnName, "#14:1");// calc(formField, dataRow, columnName, id));
            //throw new UnsupportedOperationException("field '" + columnName + "' is defined in old style, not supported any more");
        }
    }

    @Deprecated
    private String calc(FormField formField, Map<String, Object> dataRow, String columnName, Object id) {
        if (formField != null) {
           // String processed = formField.process(response, dataRow, columnName, id);
//            processed = checkPrefix(formField, dataRow, processed, id);
//            processed = checkPostfix(formField, dataRow, processed, id);
            return "";//processed;
        }
        return dataRow.get(columnName) != null ? dataRow.get(columnName).toString() : "";
    }

    private String calc(@NonNull ClassFieldModel field, Map<String, Object> dataRow, String columnName, Object id,
            R r) {
        String processed = new CellRendererHelper(field, response).render(dataRow.get(columnName), id, r);
        // processed = checkPrefix(field, dataRow, processed, id);
        // processed = checkPostfix(field, dataRow, processed, id);
        return processed;
    }

    public List<Breadcrumb> getBreadcrumbs() {
        return new Breadcrumbs().create(resource);
    }

    public List<TreeStructure> getTreeStructure() {
        return TreeStructure.from(resource);
    }

    public List<RepresentationLink> getRepresentations() {
        Set<String> supportedMediaTypes = ResourceUtils.getSupportedMediaTypes(resource, getParameterizedType());
        return supportedMediaTypes.stream().filter(mediaType -> {
            return !mediaType.equals("event-stream");
        }).map(mediaType -> {
            return new RepresentationLink(mediaType, resource.getCurrentEntity());
        }).collect(Collectors.toList());
    }

    public List<io.skysail.api.links.Link> getLinks() {
        return resource.getAuthorizedLinks();
    }

    public List<io.skysail.api.links.Link> getCollectionLinks() {
        return resource.getAuthorizedLinks().stream()
                .filter(l -> LinkRelation.COLLECTION.equals(l.getRel()) || LinkRelation.SELF.equals(l.getRel()))
                .collect(Collectors.toList());
    }

    public List<io.skysail.api.links.Link> getCreateFormLinks() {
        return resource.getAuthorizedLinks().stream().filter(l -> LinkRelation.CREATE_FORM.equals(l.getRel()))
                .collect(Collectors.toList());
    }

    public List<io.skysail.api.links.Link> getResourceLinks() throws Exception {
        return resource.getAuthorizedLinks().stream().filter(l -> l.isShowAsButtonInHtml())
                .collect(Collectors.toList());
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

        switch (theme.getVariant()) {
        case BOOTSTRAP:
            return getBootstrapPagination(pages, page);
        case UIKIT:
            return getUiKitPagination(pages, page);
        case PURECSS:
            return getPurecssPagination(pages, page);
        case W2UI:
            return "";// getPurecssPagination(pages, page);
        default:
            return "";
        }
    }

    private static String getBootstrapPagination(int pages, int page) {
        StringBuilder sb = new StringBuilder();
        sb.append("<nav>");
        sb.append("<ul class='pagination'>");
        String cssClass = (1 == page) ? "class='disabled'" : "";

        sb.append("<li " + cssClass + "><a href='?").append(SkysailServerResource.PAGE_PARAM_NAME)
                .append("=" + (page - 1)
                        + "'><span aria-hidden='true'>&laquo;</span><span class='sr-only'>Previous</span></a></li>");
        for (int i = 1; i <= pages; i++) {
            cssClass = (i == page) ? " class='active'" : "";
            sb.append("<li" + cssClass + "><a href='?").append(SkysailServerResource.PAGE_PARAM_NAME)
                    .append("=" + i + "'>" + i + "</a></li>");
        }
        cssClass = (pages == page) ? " class='disabled'" : "";
        sb.append("<li" + cssClass + "><a href='?").append(SkysailServerResource.PAGE_PARAM_NAME).append("="
                + (page + 1) + "'><span aria-hidden='true'>&raquo;</span><span class='sr-only'>Next</span></a></li>");
        sb.append("</ul>");
        sb.append("</nav>");
        return sb.toString();
    }

    private static String getUiKitPagination(int pages, int page) {
        return "<ul class='uk-pagination' data-uk-pagination='{pages:" + pages + ", currentPage:" + page + "}'></ul>";
    }

    private static String getPurecssPagination(int pages, int page) {
        return "<ul class='uk-pagination' data-uk-pagination='{pages:" + pages + ", currentPage:" + page + "}'></ul>";
    }

    public String getStatus() {
        Status status = resource.getStatus();
        return status.getCode() + ": " + status.getDescription();
    }

    public String getResourceSimpleName() {
        return resource.getClass().getSimpleName();
    }

    public String getTest() {
        return resource.getClass().getName();
    }

    public String getEntityname() {
        return parameterizedType.getName();
    }

    public String getContextPath() {
        SkysailApplication application = resource.getApplication();
        return new StringBuilder("/").append(application.getName()).append(application.getApiVersion().getVersionPath())
                .toString();
    }

    public String getEntityType() {
        return resource.getEntityType().replace("<", "&lt;").replace(">", "&gt;");
    }

    public String getEntityTypeGithubLink() {
        String result = resource.getEntityType().replace("<", "&lt;").replace(">", "&gt;");
        if (!result.contains("skysail")) {
            return "#";
        }
        String bundleName = resource.getApplication().getBundle().getSymbolicName();
        StringBuilder sb = new StringBuilder("https://github.com/evandor/skysail-framework/tree/master/")
                .append(bundleName).append("/").append("src/")
                .append(resource.getEntityType().replace("List of ", "").replace(".", "/")).append(".java").append("'");
        return sb.toString();
    }

    public Map<String, String> getHeaders() {
        Series<Header> headers = HeadersUtils.getHeaders(resource.getResponse());
        Map<String, String> headersAsMap = headers.stream()
                .collect(Collectors.toMap(Header::getName, Header::getValue));
        return headersAsMap;
    }

    public String getHeaderValue(String key) {
        Series<Header> headers = HeadersUtils.getHeaders(resource.getResponse());
        return headers.stream().filter(h -> h.getName().equals(key)).findFirst().map(h -> h.getValue()).orElse("");
    }

    public boolean isForm() {
        return response.isForm();
    }

    public boolean isRelationTargetList() {
        return response.isRelationTargetList();
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

    public void setSearchService(SearchService searchService) {
        this.searchService = searchService;
    }

    public void setMenuItemProviders(Set<MenuItemProvider> menuProviders) {
        this.services = new STServicesWrapper(menuProviders, searchService, resource);
    }

    private void addAssociatedLinks(List<Map<String, Object>> theData) {
        if (!(getResource() instanceof ListServerResource)) {
            return;
        }
        ListServerResource<?> listServerResource = (ListServerResource<?>) getResource();
        List<io.skysail.api.links.Link> links = listServerResource.getLinks();
        List<Class<? extends SkysailServerResource<?>>> entityResourceClass = listServerResource
                .getAssociatedServerResources();
        if (entityResourceClass != null) {
            List<Map<String, Object>> sourceAsList = theData;
            for (Map<String, Object> dataRow : sourceAsList) {
                addLinks(links, dataRow);
            }
        }
    }

    private void addLinks(List<io.skysail.api.links.Link> links, Map<String, Object> dataRow) {
        String id = guessId(dataRow);
        if (id == null) {
            return;
        }

        String linkshtml = links.stream().filter(l -> id.equals(l.getRefId())).map(link -> {
            StringBuilder sb = new StringBuilder();

            if (link.getImage(MediaType.TEXT_HTML) != null) {
                sb.append("<a href='").append(link.getUri())
                    .append("' title='").append(link.getTitle())
                    .append("' alt='").append(link.getAlt()).append("'>")
                    .append("<span class='glyphicon glyphicon-").append(link.getImage(MediaType.TEXT_HTML)).append("' aria-hidden='true'></span>")
                    .append("</a>");
            } else {
                sb.append("<a href='").append(link.getUri()).append("' title='").append(link.getAlt()).append("'>").append(link.getTitle()).append("</a>");
            }
            return sb.toString();
        }).collect(Collectors.joining("&nbsp;&nbsp;"));

        dataRow.put("_links", linkshtml);
    }

    private Optional<FieldModel> getDomainField(String columnName) {
        ApplicationModel applicationModel = resource.getApplication().getApplicationModel();
        io.skysail.domain.core.EntityModel entity = applicationModel.getEntity(parameterizedType.getName());
        if (entity == null) {
            return Optional.empty();
        }
        return Optional.ofNullable(entity.getField(columnName));
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
        } else if (entity.get("name") != null) {
            return entity.get("name").toString();
        } else {
            return "";
        }
    }

    public List<FormField> getFormfields() {
        ApplicationModel applicationModel = resource.getApplication().getApplicationModel();
        io.skysail.domain.core.EntityModel entity = applicationModel.getEntity(parameterizedType.getName());

        return new ArrayList<FormField>(fields.values());
    }

    public Map<String, List<FormField>> getTabFields() {
        Map<String, List<FormField>> result = new HashMap<>();
        for (FormField field : fields.values()) {
            PostView postViewAnnotation = field.getPostViewAnnotation();
            String tab = postViewAnnotation == null ? "more..." : postViewAnnotation.tab();
            if ("".equals(tab)) {
                tab = "more...";
            }
            if (result.containsKey(tab)) {
                result.get(tab).add(field);
            } else {
                List<FormField> tabFields = new ArrayList<>();
                tabFields.add(field);
                result.put(tab, tabFields);
            }
        }
        return result;
    }

    // public List<FieldModel> getFields() {
    // ApplicationModel applicationModel =
    // resource.getApplication().getApplicationModel();
    // EntityModel entity =
    // applicationModel.getEntity(parameterizedType.getName());
    // return new ArrayList<FieldModel>(entity.getFields().values());
    // }

    public boolean isConstraintViolationsResponse() {
        return response instanceof ConstraintViolationsResponse;
    }

    public String getClasslevelViolationMessage() {
        if (!isConstraintViolationsResponse()) {
            return null;
        }
        Set<ConstraintViolationDetails> violations = ((ConstraintViolationsResponse<?>) response).getViolations();
        String msg = violations.stream().filter(v -> {
            return v.getPropertyPath().equals("");
        }).map(v -> {
            return v.getMessage();
        }).collect(Collectors.joining(", "));
        return StringUtils.isEmpty(msg) ? null : msg;
    }

    public String getFormTarget() {
        if (response instanceof ConstraintViolationsResponse) {
            Reference actionReference = ((ConstraintViolationsResponse<?>) response).getActionReference();
            return actionReference.toString();
        }
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
            return "../" + ((Identifiable) entity).getId().replace("#", "");
        }
        return "../" + ((FormResponse<?>) response).getId();
    }

    public String getRedirectBackTo() {
        if (response instanceof FormResponse) {
            return ((FormResponse<?>) response).getRedirectBackTo();
        }
        return null;
    }

    public String getDateFormat() {
        if (dateFormat != null && dateFormat instanceof SimpleDateFormat) {
            // return ((SimpleDateFormat) dateFormat).toPattern().replace("d",
            // "D").replace("y", "Y");
            return "YYYY-MM-DD";
        }
        return "";
    }

    public boolean isUseTabs() {
        return !getTabs().isEmpty();
    }

    public List<Tab> getTabs() {
        ApplicationModel applicationModel = resource.getApplication().getApplicationModel();
        ClassEntityModel entity = (ClassEntityModel) applicationModel.getEntity(parameterizedType.getName());
        Set<Tab> tabsFromEntityDefinition = entity.getTabs();
        List<Tab> tabDefinitions = resource.getTabs();

        List<Tab> result = new ArrayList<>();
        tabDefinitions.stream().forEach(tabDef -> {
            result.add(tabDef);
            tabsFromEntityDefinition.remove(tabDef);
        });

        tabsFromEntityDefinition.stream().forEach(tabDef -> {
            result.add(tabDef);
        });

        return new ArrayList<Tab>(result);
    }

    public boolean isShowBreadcrumbs() {
        return GuiFeatures.SHOW_BREADCRUMBS.isActive();
    }

    public ApplicationModel getApplicationModel() {
        return resource.getApplication().getApplicationModel();
    }
    
    private String checkPrefix(FormField formField, Map<String, Object> dataRow, String processed, Object id) {
        ApplicationModel applicationModel = resource.getApplication().getApplicationModel();
        io.skysail.domain.core.EntityModel entity = applicationModel.getEntity(parameterizedType.getName());

        if (this.resource instanceof ListServerResource) {
            if (formField.getListViewAnnotation() != null && !formField.getListViewAnnotation().prefix().equals("")) {
                throw new UnsupportedOperationException();

//                Object prefix = calc(fields.get(formField.getListViewAnnotation().prefix()), dataRow,
//                        formField.getListViewAnnotation().prefix(), id);
//                if (prefix != null) {
//                    return prefix + "&nbsp;" + processed;
//                }
            }
        }
        return processed;
    }

    private String checkPostfix(FormField formField, Map<String, Object> dataRow, String processed, Object id) {
        return processed;
    }

}
