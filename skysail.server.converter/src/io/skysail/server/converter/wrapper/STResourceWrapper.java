package io.skysail.server.converter.wrapper;

import io.skysail.server.converter.Breadcrumb;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import lombok.extern.slf4j.Slf4j;

import org.apache.commons.lang.StringUtils;
import org.restlet.Request;
import org.restlet.data.Header;
import org.restlet.data.MediaType;
import org.restlet.data.Reference;
import org.restlet.data.Status;
import org.restlet.engine.Engine;
import org.restlet.engine.converter.ConverterHelper;
import org.restlet.engine.resource.VariantInfo;
import org.restlet.representation.Variant;
import org.restlet.resource.ServerResource;
import org.restlet.routing.Route;
import org.restlet.util.RouteList;
import org.restlet.util.Series;

import de.twenty11.skysail.api.responses.Linkheader;
import de.twenty11.skysail.server.app.SkysailApplication;
import de.twenty11.skysail.server.core.restlet.ApplicationContextId;
import de.twenty11.skysail.server.core.restlet.PostEntityServerResource;
import de.twenty11.skysail.server.core.restlet.PutEntityServerResource;
import de.twenty11.skysail.server.core.restlet.ResourceContextId;
import de.twenty11.skysail.server.core.restlet.SkysailServerResource;
import de.twenty11.skysail.server.utils.HeadersUtils;

@Slf4j
public class STResourceWrapper {

    private SkysailServerResource<?> resource;
    private Object source;

    public STResourceWrapper(Object source, SkysailServerResource<?> resource) {
        this.source = source;
        this.resource = resource;
    }

    public STApplicationWrapper getApplication() {
        return new STApplicationWrapper(resource);
    }

    public List<Linkheader> getLinkheader() throws Exception {
        return resource.getLinkheaderAuthorized();
    }

    public List<Breadcrumb> getBreadcrumbs() {

        List<Breadcrumb> breadcrumbs = new ArrayList<Breadcrumb>();
        breadcrumbs.add(new Breadcrumb("/", null, "<span class='glyphicon glyphicon-home'></span>", null));

        Reference reference = resource.getReference();
        SkysailApplication application = resource.getApplication();
        String img = application.getFromContext(ApplicationContextId.IMG);
        String imgHtml = img != null ? "<img src='" + img + "'>" : "";
        RouteList routes = application.getRoutes();

        List<String> segments = getCleanedSegments(reference);

        String path = "";
        Route match = null;
        if (segments != null && segments.size() > 0) {
            String text = imgHtml + " " + resource.getApplication().getName();
            breadcrumbs.add(new Breadcrumb("/" + resource.getApplication().getName(), null, text, false));
        }
        for (int i = 1; i < segments.size(); i++) {
            path = path + "/" + segments.get(i);

            Request request = resource.getRequest();
            request.setResourceRef(new Reference(path));
            Route best = routes.getBest(request, resource.getResponse(), 0.5f);
            if (best != match) {
                match = best;
                if (i < segments.size() - 1) {
                    breadcrumbs.add(new Breadcrumb("/" + resource.getApplication().getName() + path, null, limit(
                            segments.get(i), 12), false));
                } else {
                    breadcrumbs.add(new Breadcrumb(null, null, segments.get(i), true));
                }
            }
        }
        return breadcrumbs;
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

    public boolean isPostEntityServerResource() {
        return resource instanceof PostEntityServerResource;
    }

    public boolean isPutEntityServerResource() {
        return resource instanceof PutEntityServerResource;
    }

    @Override
    public String toString() {
        return resource.toString();
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

    public Set<String> getSupportedMediaTypes() {
        List<Variant> supportedVariants = Collections.emptyList();
        Set<MediaType> supportedMediaTypes = new HashSet<>();
        if (resource instanceof ServerResource) {
            supportedVariants = ((ServerResource) resource).getVariants();
            for (Variant variant : supportedVariants) {
                supportedMediaTypes.add(variant.getMediaType());
            }
        }

        Set<String> mediaTypes = new HashSet<String>();
        List<ConverterHelper> registeredConverters = Engine.getInstance().getRegisteredConverters();
        for (ConverterHelper ch : registeredConverters) {
            List<VariantInfo> variants;
            try {
                variants = ch.getVariants(source.getClass());
                if (variants == null) {
                    continue;
                }
                for (VariantInfo variantInfo : variants) {
                    if (supportedMediaTypes.contains(variantInfo.getMediaType())) {
                        String subType = variantInfo.getMediaType().getSubType();
                        mediaTypes.add(subType.equals("*") ? variantInfo.getMediaType().getName() : subType);
                    }
                }
            } catch (IOException e) {
                log.error(e.getMessage(), e);
            }
        }
        return mediaTypes;
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
        if (pagesAsString != null) {
            page = Integer.parseInt(pageAsString);
        }

        StringBuilder sb = new StringBuilder();
        sb.append("<nav>");
        sb.append("<ul class='pagination'>");
        String cssClass = (1 == page) ? "class='disabled'" : "";

        sb.append("<li " + cssClass + "><a href='?page=" + (page - 1)
                + "'><span aria-hidden='true'>&laquo;</span><span class='sr-only'>Previous</span></a></li>");
        for (int i = 1; i <= pages; i++) {
            cssClass = (i == page) ? " class='active'" : "";
            sb.append("<li" + cssClass + "><a href='?page=" + i + "'>" + i + "</a></li>");
        }
        cssClass = (pages == page) ? " class='disabled'" : "";
        sb.append("<li" + cssClass + "><a href='?page=" + (page + 1)
                + "'><span aria-hidden='true'>&raquo;</span><span class='sr-only'>Next</span></a></li>");
        sb.append("</ul>");
        sb.append("</nav>");
        return sb.toString();
    }

    private List<String> getCleanedSegments(Reference reference) {
        List<String> segments = reference.getSegments();
        List<String> results = new ArrayList<String>();
        for (String segment : segments) {
            if (!StringUtils.isBlank(segment)) {
                results.add(segment);
            }
        }
        return results;
    }

    private String limit(String value, int lengthLimit) {
        if (value == null) {
            return "";
        }
        if (value.length() <= lengthLimit) {
            return value;
        }
        return value.substring(0, lengthLimit - 3).concat("...");
    }

}
