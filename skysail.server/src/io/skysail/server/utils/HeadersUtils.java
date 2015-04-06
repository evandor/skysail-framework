package io.skysail.server.utils;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

import org.restlet.Response;
import org.restlet.data.Header;
import org.restlet.util.Series;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class HeadersUtils {

    public static class ValueComparator implements Comparator<String> {

        private Map<String, Float> base;

        public ValueComparator(Map<String, Float> base) {
            this.base = base;
        }

        @Override
        public int compare(String a, String b) {
            if (base.get(a) >= base.get(b)) {
                return -1;
            } else {
                return 1;
            }
        }
    }

    private static final Logger logger = LoggerFactory.getLogger(HeadersUtils.class);

    public static final String PAGINATION_PAGES = "X-Pagination-Pages";
    public static final String PAGINATION_PAGE = "X-Pagination-Page";
    public static final String PAGINATION_HITS = "X-Pagination-Hits";

    public static List<String> parseAcceptedLanguages(String acceptLanguageHeader) {
        if (acceptLanguageHeader == null || acceptLanguageHeader.trim().length() == 0) {
            return Collections.emptyList();
        }
        Map<String, Float> weightedMap = new HashMap<>();
        String[] mainParts = acceptLanguageHeader.split(",");
        for (String part : mainParts) {
            String[] subpart = part.split(";");
            if (subpart.length == 1) {
                weightedMap.put(subpart[0].trim(), 1.0F);
            } else {
                String[] weightSplit = subpart[1].split("=");
                if (weightSplit.length == 0 || !"q".equals(weightSplit[0])) {
                    logger.warn("could not parse Accepted-Langauge Header '{}'", acceptLanguageHeader);
                    continue;
                }
                weightedMap.put(subpart[0].trim(), Float.valueOf(weightSplit[1]));
            }
        }

        ValueComparator comparator = new ValueComparator(weightedMap);
        Map<String, Float> sortedMap = new TreeMap<>(comparator);
        sortedMap.putAll(weightedMap);

        return new ArrayList<String>(sortedMap.keySet());
    }

    public static Series<Header> addToHeaders(Response response, String headername, String headervalue) {
        Series<Header> headers = getHeaders(response);
        headers.add(headername, headervalue);
        return headers;
    }

    @SuppressWarnings({ "unchecked", "rawtypes" })
    public static Series<Header> getHeaders(Response response) {
        Series<Header> responseHeaders = (Series<Header>) response.getAttributes().get("org.restlet.http.headers");
        if (responseHeaders == null) {
            responseHeaders = new Series(Header.class);
            response.getAttributes().put("org.restlet.http.headers", responseHeaders);
        }
        return responseHeaders;
    }

}
