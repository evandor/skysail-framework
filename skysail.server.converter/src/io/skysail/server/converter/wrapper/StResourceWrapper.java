package io.skysail.server.converter.wrapper;

import io.skysail.api.favorites.FavoritesService;
import io.skysail.api.links.Link;
import io.skysail.server.converter.Breadcrumb;
import io.skysail.server.converter.RepresentationLink;
import io.skysail.server.restlet.resources.PostEntityServerResource;
import io.skysail.server.restlet.resources.PutEntityServerResource;
import io.skysail.server.restlet.resources.SkysailServerResource;
import io.skysail.server.utils.HeadersUtils;
import io.skysail.server.utils.ResourceUtils;

import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import lombok.extern.slf4j.Slf4j;

import org.restlet.data.Header;
import org.restlet.data.Status;
import org.restlet.util.Series;

import de.twenty11.skysail.server.core.restlet.ResourceContextId;

@Slf4j
public class StResourceWrapper {

    private SkysailServerResource<?> resource;
    private Object source;
    private FavoritesService favoritesService;

    public StResourceWrapper(Object source, SkysailServerResource<?> resource, FavoritesService favoritesService) {
        this.source = source;
        this.resource = resource;
        this.favoritesService = favoritesService;
    }

    public STApplicationWrapper getApplication() {
        return new STApplicationWrapper(resource);
    }

    public List<Link> getLinks() throws Exception {
        return resource.getAuthorizedLinks();
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
        return ResourceUtils.getSupportedMediaTypes(resource, source);
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

    public String getMetaRefresh() {
        String target = resource.getMetaRefreshTarget();
        if (target == null) {
            return "";
        }
        return "<meta http-equiv=\"Refresh\" content=\"0; url=" + target + "\" />";
    }

}
