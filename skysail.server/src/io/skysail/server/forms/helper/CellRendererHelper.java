package io.skysail.server.forms.helper;

import java.net.URL;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Optional;
import java.util.Set;

import io.skysail.api.links.Link;
import io.skysail.api.responses.ListServerResponse;
import io.skysail.api.responses.SkysailResponse;
import io.skysail.domain.core.FieldModel;
import io.skysail.server.domain.jvm.ClassFieldModel;
import io.skysail.server.forms.ListView;
import io.skysail.server.restlet.resources.SkysailServerResource;

/**
 * For the given field definition (formField) and (skysail) response, the provided cellData object is being rendered as
 * a "meaningful" string according to the following logic:
 *
 * - if the cellData is null, "" is returned.
 * - otherwise, the cellData is turned into a string, applying some logic depending on the cellDatas type.
 *
 *
 */
public class CellRendererHelper {

    private SkysailResponse<?> response;
    private ClassFieldModel field;

    public CellRendererHelper(ClassFieldModel field, SkysailResponse<?> response) {
        this.field = field;
        this.response = response;
    }

    public String render(Object cellData, Object identifier, SkysailServerResource<?> r) {
        if (cellData == null) {
            return "";
        }
        String string = toString(cellData);
        if (response instanceof ListServerResponse) {
            return handleListView(string, field, identifier, r);
        }
        return string;
    }

    private String handleListView(String string, ClassFieldModel f, Object identifier, SkysailServerResource<?> r) {
        if (URL.class.equals(f.getType())) {
            string = "<a href='" + string + "' target=\"_blank\">" + truncate(f, string, true) + "</a>";
        } else if (hasListViewLink(f)) {
            string = renderListViewLink(string, f, identifier, r);
        } else {
            return truncate(f, string, false);
        }
        return string;
    }

    private boolean hasListViewLink(ClassFieldModel f) {
        if (f.getListViewLink() == null) {
            return false;
        }
        return !f.getListViewLink().equals(ListView.DEFAULT.class);
    }

    private String renderListViewLink(String string, ClassFieldModel f, Object id, SkysailServerResource<?> r) {
        Class<? extends SkysailServerResource<?>> linkedResource = f.getListViewLink();
        List<Link> links = r.getLinks();
        if (links != null && id != null) {
            Optional<Link> findFirst = links.stream().filter(l -> {
                String idAsString = id != null ? id.toString().replace("#", "") : "";
                return linkedResource.equals(l.getCls()) && idAsString.equals(l.getRefId());
            }).findFirst();
            if (findFirst.isPresent()) {
                string = "<a href='" + findFirst.get().getUri() + "'><b>" + truncate(f, string, false) + "</b></a>";
            }
        }
        return string;
    }

    private static String truncate(FieldModel f, String string, boolean withoutHtml) {
        if (f.getTruncateTo() == null) {
            return string;
        }
        if (f.getTruncateTo() > 3) {
            String oldValue = string;
            if (string != null && string.length() > f.getTruncateTo()) {
                if (withoutHtml) {
                    return oldValue.substring(0, f.getTruncateTo() - 3) + "...";
                }
                return "<span title='" + oldValue + "'>"
                        + oldValue.substring(0, f.getTruncateTo() - 3) + "...</span>";
            }
        }
        return string;
    }

    private static String toString(Object cellData) {
        if (cellData instanceof List) {
            return "#"+Integer.toString(((List<?>) cellData).size());
        }
        if (cellData instanceof Set) {
            return Integer.toString(((Set<?>) cellData).size());
        }
        if (cellData instanceof Long && ((Long) cellData) > 1000000000) {
            // assuming timestamp for now
            return new SimpleDateFormat("yyyy-MM-dd").format(new Date((Long) cellData));
        }
        if (!(cellData instanceof String)) {
            return cellData.toString();
        }
        String string = (String) cellData;
        return string.replace("\r", "&#13;").replace("\n", "&#10;");
    }

}
