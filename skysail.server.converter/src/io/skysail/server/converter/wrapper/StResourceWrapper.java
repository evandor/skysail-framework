package io.skysail.server.converter.wrapper;

import io.skysail.api.favorites.FavoritesService;
import io.skysail.server.model.ResourceModel;
import io.skysail.server.restlet.resources.SkysailServerResource;
import io.skysail.server.utils.ResourceUtils;

import java.util.Set;

import org.restlet.data.Status;

public class StResourceWrapper {

    private FavoritesService favoritesService;
    private ResourceModel<SkysailServerResource<?>, ?> model;

    public StResourceWrapper(ResourceModel<SkysailServerResource<?>,?> model, FavoritesService favoritesService) {
        this.model = model;
        this.favoritesService = favoritesService;
    }

    public STApplicationWrapper getApplication() {
        return new STApplicationWrapper(model.getResource());
    }

    public String getClassname() {
        return model.getResource().getClass().getName();
    }

    public String getEntityType() {
        String result = model.getResource().getEntityType().replace("<", "&lt;").replace(">", "&gt;");
        if (!result.contains("skysail")) {
            return result;
        }
        // https://github.com/evandor/skysail/blob/master/skysail.server.app.i18n/src/de/twenty11/skysail/server/app/i18n/messages/BundleMessages.java
        String bundleName = model.getResource().getApplication().getBundle().getSymbolicName();
        StringBuilder sb = new StringBuilder("<a target='_blank' href='https://github.com/evandor/skysail/blob/master/")
                //
                .append(bundleName).append("/").append("src/")
                //
                .append(model.getResource().getEntityType().replace("List of ", "").replace(".", "/")).append(".java")
                .append("'>View on github</a>");
        return result + "&nbsp;" + sb.toString();
    }

    @Override
    public String toString() {
        return model.getResource().toString();
    }

    public String getStatus() {
        Status status = model.getResource().getStatus();
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
        return ResourceUtils.getSupportedMediaTypes(model.getResource(), model.getConvertedSource());
    }


}
