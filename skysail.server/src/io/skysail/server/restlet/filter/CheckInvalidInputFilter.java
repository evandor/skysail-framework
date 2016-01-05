package io.skysail.server.restlet.filter;

import java.lang.reflect.Field;
import java.util.*;

import org.owasp.html.*;
import org.restlet.*;
import org.restlet.data.*;

import de.twenty11.skysail.server.core.restlet.Wrapper;
import io.skysail.domain.Identifiable;
import io.skysail.domain.html.*;
import io.skysail.server.app.SkysailApplication;
import io.skysail.server.restlet.resources.*;
import io.skysail.server.utils.ReflectionUtils;
import lombok.NoArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@NoArgsConstructor
public class CheckInvalidInputFilter<R extends SkysailServerResource<?>, T extends Identifiable> extends AbstractResourceFilter<R, T> {

    private static HtmlPolicyBuilder noHtmlPolicyBuilder = new HtmlPolicyBuilder();

    private SkysailApplication application;

    public CheckInvalidInputFilter(SkysailApplication application) {
        this.application = application;
    }

    @Override
    public FilterResult doHandle(R resource, Wrapper<T> responseWrapper) {
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
                    io.skysail.domain.html.Field formField = field.getAnnotation(io.skysail.domain.html.Field.class);
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

        List<Field> fields = ReflectionUtils.getInheritedFields(resource.getParameterizedType());

        for (int i = 0; i < form.size(); i++) {
            Parameter parameter = form.get(i);
            String originalValue = parameter.getValue();


            HtmlPolicyBuilder htmlPolicyBuilder = detectHtmlPolicyBuilder(resource, parameter, fields);

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
        htmlPolicyBuilder.allowUrlProtocols("http", "https", "file");
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

    private HtmlPolicyBuilder detectHtmlPolicyBuilder(R resource, Parameter parameter, List<Field> fields) {
        HtmlPolicyBuilder htmlPolicyBuilder = noHtmlPolicyBuilder;
        Optional<Field> found = fields.stream().filter(f -> f.getName().equals(parameter.getName())).findFirst();
        if (found.isPresent()) {
            io.skysail.domain.html.Field fieldAnnotation = found.get().getAnnotation(io.skysail.domain.html.Field.class);
            if (fieldAnnotation != null && fieldAnnotation.htmlPolicy() != null) {
                htmlPolicyBuilder = createHtmlPolicyBuilder(fieldAnnotation.htmlPolicy());
            }
        }
        return htmlPolicyBuilder;
    }

}
