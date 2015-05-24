package io.skysail.server.queryfilter.pagination;

import io.skysail.server.restlet.resources.SkysailServerResource;
import io.skysail.server.utils.HeadersUtils;
import lombok.*;
import lombok.extern.slf4j.Slf4j;

import org.restlet.*;
import org.restlet.data.Header;
import org.restlet.util.Series;

import de.twenty11.skysail.server.core.restlet.utils.CookiesUtils;

@Slf4j
@NoArgsConstructor
public class Pagination {

    private static final long DEFAULT_LINES_PER_PAGE = 10;

    @Getter
    private int page = 1;

    private Request request;

    public Pagination(Request request, Response response, long entityCount) {
        this.request = request;
        Series<Header> headers = HeadersUtils.getHeaders(response);

        headers.add(new Header(HeadersUtils.PAGINATION_PAGES, getPagesCount(entityCount)));
        headers.add(new Header(HeadersUtils.PAGINATION_HITS, Long.toString(entityCount)));

        String pageQueryParameter = (String) request.getAttributes().get(SkysailServerResource.PAGE_PARAM_NAME);
        if (pageQueryParameter == null || pageQueryParameter.trim().length() == 0) {
            return;
        }
        try {
            page = Integer.parseInt(pageQueryParameter);
            headers.add(new Header(HeadersUtils.PAGINATION_PAGE, Integer.toString(page)));
        } catch (NumberFormatException e) {
            log.debug(e.getMessage(), e);
        }
    }

    private String getPagesCount(long entityCount) {
        return Long.toString(1 + Math.floorDiv(entityCount, 1 + DEFAULT_LINES_PER_PAGE));
    }

    public long getLinesPerPage() {
        String entriesPerPage = CookiesUtils.getEntriesPerPageFromCookie(request);
        if (entriesPerPage != null) {
            try {
                return Long.parseLong(entriesPerPage);
            } catch (NumberFormatException e) {
                log.info("could not parse {} as 'entriesPerPage' data", entriesPerPage);
            }
        }
        return DEFAULT_LINES_PER_PAGE;
    }

}
