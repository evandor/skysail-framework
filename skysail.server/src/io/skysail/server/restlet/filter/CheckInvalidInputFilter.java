package io.skysail.server.restlet.filter;

import io.skysail.api.forms.AllowedAttribute;
import io.skysail.api.forms.HtmlPolicy;
import io.skysail.server.app.SkysailApplication;
import io.skysail.server.restlet.resources.EntityServerResource;
import io.skysail.server.restlet.resources.SkysailServerResource;
import io.skysail.server.utils.ReflectionUtils;

import java.lang.reflect.Field;
import java.util.List;

import lombok.extern.slf4j.Slf4j;

import org.owasp.html.Handler;
import org.owasp.html.HtmlPolicyBuilder;
import org.owasp.html.HtmlSanitizer;
import org.owasp.html.HtmlStreamRenderer;
import org.restlet.Request;
import org.restlet.Response;
import org.restlet.data.Form;
import org.restlet.data.Parameter;

import de.twenty11.skysail.server.core.restlet.ResponseWrapper;

@Slf4j
public class CheckInvalidInputFilter<R extends SkysailServerResource<T>, T> extends AbstractResourceFilter<R, T> {

    private static HtmlPolicyBuilder noHtmlPolicyBuilder = new HtmlPolicyBuilder();

    private SkysailApplication application;

    public CheckInvalidInputFilter() {
    }

    public CheckInvalidInputFilter(SkysailApplication application) {
        this.application = application;
    }

    @Override
    public FilterResult doHandle(R resource, ResponseWrapper<T> responseWrapper) {
        log.debug("entering {}#doHandle", this.getClass().getSimpleName());

        // do in "before"?
        Response response = responseWrapper.getResponse();
        Form form = (Form) response.getRequest().getAttributes().get(EntityServerResource.SKYSAIL_SERVER_RESTLET_FORM);

        // TODO check: Data from entity (not from form) is not validated!
        if (containsInvalidInput(response.getRequest(), resource, form)) {
            log.info("Input was sanitized");
        }
        super.doHandle(resource, responseWrapper);
        return FilterResult.CONTINUE;
    }

    // TODO ugly! Where are the tests???
    private boolean containsInvalidInput(Request request, R resource, Form form) {
        boolean foundInvalidInput = false;
        if (form == null) {
            Object entityAsObject = request.getAttributes().get(EntityServerResource.SKYSAIL_SERVER_RESTLET_ENTITY);
            if (entityAsObject != null) {
                @SuppressWarnings("unchecked")
                T entity = (T) entityAsObject;
                List<Field> fields = ReflectionUtils.getInheritedFields(entity.getClass());

                for (Field field : fields) {
                    io.skysail.api.forms.Field formField = field.getAnnotation(io.skysail.api.forms.Field.class);
                    if (formField == null) {
                        continue;
                    }
                    HtmlPolicy htmlPolicy = formField.htmlPolicy();
                    // List<String> allowedElements =
                    // htmlPolicy.getAllowedElements();
                    HtmlPolicyBuilder htmlPolicyBuilder = createHtmlPolicyBuilder(htmlPolicy);

                    field.setAccessible(true);
                    String originalValue = "";
                    try {
                        Object fieldValue = field.get(entity);
                        if (!(fieldValue instanceof String)) {
                            continue;
                        }
                        originalValue = (String) fieldValue;
                    } catch (Exception e) {
                        log.error(e.getMessage(), e);
                    }

                    StringBuilder sb = new StringBuilder();
                    HtmlSanitizer.Policy policy = createPolicy(htmlPolicyBuilder, sb);
                    HtmlSanitizer.sanitize(originalValue, policy);
                    String sanitizedHtml = sb.toString();
                    if (!sanitizedHtml.equals(originalValue)) {
                        try {
                            field.set(entity, sanitizedHtml);
                            log.info("sanitized '{}' to '{}'", originalValue, sanitizedHtml);
                        } catch (Exception e) {
                            log.error(e.getMessage(), e);
                        }
                        foundInvalidInput = true;
                    }
                }
                return foundInvalidInput;
            }
            return false;
        }
        for (int i = 0; i < form.size(); i++) {
            Parameter parameter = form.get(i);
            String originalValue = parameter.getValue();

            HtmlPolicyBuilder htmlPolicyBuilder = detectHtmlPolicyBuilder(resource, parameter);

            StringBuilder sb = new StringBuilder();
            // http://stackoverflow.com/questions/12558471/how-to-allow-specific-characters-with-owasp-html-sanitizer
            HtmlSanitizer.Policy policy = createPolicy(htmlPolicyBuilder, sb);
            HtmlSanitizer.sanitize(originalValue, policy);
            String sanitizedHtml = sb.toString();
            if (!sanitizedHtml.equals(originalValue)) {
                foundInvalidInput = true;
            }
            parameter.setValue(sanitizedHtml.trim());
        }
        return foundInvalidInput;
    }

    private HtmlPolicyBuilder createHtmlPolicyBuilder(HtmlPolicy htmlPolicy) {
        HtmlPolicyBuilder htmlPolicyBuilder = new HtmlPolicyBuilder();
        List<String> allowedElements = htmlPolicy.getAllowedElements();
        List<AllowedAttribute> allowedAttributes = htmlPolicy.getAllowedAttributes();
        htmlPolicyBuilder.allowElements(allowedElements.toArray(new String[allowedElements.size()]));

        for (AllowedAttribute att : allowedAttributes) {
            htmlPolicyBuilder.allowAttributes(att.getName()).onElements(att.getForElements());
        }
        return htmlPolicyBuilder;
    }

    private HtmlSanitizer.Policy createPolicy(HtmlPolicyBuilder htmlPolicyBuilder, StringBuilder sb) {
        HtmlSanitizer.Policy policy = htmlPolicyBuilder.build(HtmlStreamRenderer.create(sb, new Handler<String>() {
            @Override
            public void handle(String x) {
                System.out.println(x);
            }
        }));
        return policy;
    }

    private HtmlPolicyBuilder detectHtmlPolicyBuilder(R resource, Parameter parameter) {
        HtmlPolicyBuilder htmlPolicyBuilder = noHtmlPolicyBuilder;

        if (application != null && resource instanceof EntityServerResource) {
            Class<? extends Object> cls = ((EntityServerResource<T>) resource).getEntity().getClass();
            cls = resource.getParameterizedType();
            throw new IllegalAccessError();
            //htmlPolicyBuilder = application.getHtmlPolicy(cls, parameter.getName());
        }
        return htmlPolicyBuilder;
    }

}
