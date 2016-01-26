package io.skysail.server.restlet.filter;

import java.util.List;

import org.apache.commons.lang3.StringUtils;
import org.owasp.html.Handler;
import org.owasp.html.HtmlPolicyBuilder;
import org.owasp.html.HtmlSanitizer;
import org.owasp.html.HtmlStreamRenderer;

import de.twenty11.skysail.server.core.restlet.Wrapper;
import io.skysail.domain.Identifiable;
import io.skysail.domain.html.AllowedAttribute;
import io.skysail.domain.html.HtmlPolicy;
import io.skysail.server.restlet.resources.SkysailServerResource;
import lombok.extern.slf4j.Slf4j;

/**
 * Extracts common query parameters for the skysail framework; those parameters
 * have reserved names starting with an understore, e.g. "_page" or "_filter".
 *
 * <p>
 * If the parameters exist on the query, they are trimmed and added to the
 * request attributes for further consumption.
 * </p>
 */
@Slf4j
public class ExtractStandardQueryParametersResourceFilter<R extends SkysailServerResource<?>, T extends Identifiable>
        extends AbstractResourceFilter<R, T> {
    
    @Override
    protected FilterResult beforeHandle(R resource, Wrapper<T> responseWrapper) {
        addToAttributes(resource, SkysailServerResource.FILTER_PARAM_NAME);
        addToAttributes(resource, SkysailServerResource.PAGE_PARAM_NAME);
        addToAttributes(resource, SkysailServerResource.INSPECT_PARAM_NAME);
        addToAttributes(resource, SkysailServerResource.SEARCH_PARAM_NAME);
        
        adjustSearchFilter(resource);
        
        return FilterResult.CONTINUE;
    }

    private void adjustSearchFilter(R resource) {
        String search = (String) resource.getRequest().getAttributes().get(SkysailServerResource.SEARCH_PARAM_NAME);
        if (StringUtils.isEmpty(search)) {
            return;
        }
        String filter = (String) resource.getRequest().getAttributes().get(SkysailServerResource.FILTER_PARAM_NAME);
        if (StringUtils.isEmpty(filter)) {
            List<String> fields = resource.getEntityFields();
            filter = "(content=*"+search+"*)";
            resource.getRequest().getAttributes().put(SkysailServerResource.FILTER_PARAM_NAME, filter);
        } else {
            
        }
    }

    private void addToAttributes(R resource, String queryKeyName) {
        String queryValue = resource.getQueryValue(queryKeyName);
        if (queryValue != null && queryValue.trim().length() > 0) {
            String sanitizedValue = sanitize(queryValue.trim());
            resource.getRequest().getAttributes().put(queryKeyName, sanitizedValue);
        }
    }

    // duplication with CheckInvalidInputFilter
    private String sanitize(String originalValue) {
        StringBuilder sb = new StringBuilder();
        HtmlSanitizer.Policy policy = createPolicy(createHtmlPolicyBuilder(HtmlPolicy.NO_HTML), sb);
        HtmlSanitizer.sanitize(originalValue, policy);
        String sanitizedHtml = sb.toString();
        if (!sanitizedHtml.equals(originalValue)) {
            log.info(originalValue);
            log.info(sanitizedHtml);
        }
        return sanitizedHtml;
    }
    
    private HtmlPolicyBuilder createHtmlPolicyBuilder(HtmlPolicy htmlPolicy) {
        HtmlPolicyBuilder htmlPolicyBuilder = new HtmlPolicyBuilder();
        List<String> allowedElements = htmlPolicy.getAllowedElements();
        List<AllowedAttribute> allowedAttributes = htmlPolicy.getAllowedAttributes();
        htmlPolicyBuilder.allowElements(allowedElements.toArray(new String[allowedElements.size()]));

        for (AllowedAttribute att : allowedAttributes) {
            htmlPolicyBuilder.allowAttributes(att.getName()).onElements(att.getForElements());
        }
        htmlPolicyBuilder.allowUrlProtocols("http", "https", "file");
        return htmlPolicyBuilder;
    }
    
    private static HtmlSanitizer.Policy createPolicy(HtmlPolicyBuilder htmlPolicyBuilder, StringBuilder sb) {
        return htmlPolicyBuilder.build(HtmlStreamRenderer.create(sb, new Handler<String>() {
            @Override
            public void handle(String x) {
                log.info(this.getClass().getName() + ": " + x);
            }
        }));
    }
}
