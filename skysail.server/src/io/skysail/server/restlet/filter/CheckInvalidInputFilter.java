package io.skysail.server.restlet.filter;

import java.lang.reflect.Field;
import java.util.List;
import java.util.Optional;

import org.owasp.html.Handler;
import org.owasp.html.HtmlPolicyBuilder;
import org.owasp.html.HtmlSanitizer;
import org.owasp.html.HtmlStreamRenderer;
import org.restlet.Request;
import org.restlet.Response;
import org.restlet.data.Form;
import org.restlet.data.Parameter;

import de.twenty11.skysail.server.core.restlet.Wrapper;
import io.skysail.domain.Identifiable;
import io.skysail.domain.html.AllowedAttribute;
import io.skysail.domain.html.HtmlPolicy;
import io.skysail.server.app.SkysailApplication;
import io.skysail.server.restlet.resources.EntityServerResource;
import io.skysail.server.restlet.resources.SkysailServerResource;
import io.skysail.server.utils.ReflectionUtils;
import lombok.NoArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@NoArgsConstructor
public class CheckInvalidInputFilter<R extends SkysailServerResource<?>, T extends Identifiable> extends AbstractResourceFilter<R, T> {

    private static HtmlPolicyBuilder noHtmlPolicyBuilder = new HtmlPolicyBuilder();

    public CheckInvalidInputFilter(SkysailApplication application) {
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

    private boolean containsInvalidInput(Request request, R resource, Form form) {
        boolean foundInvalidInput = false;
        if (form == null) {
            Object entityAsObject = request.getAttributes().get(EntityServerResource.SKYSAIL_SERVER_RESTLET_ENTITY);
            if (entityAsObject != null) {
                @SuppressWarnings("unchecked")
                T entity = (T) entityAsObject;
                List<Field> fields = ReflectionUtils.getInheritedFields(entity.getClass());

                foundInvalidInput = handleFields(foundInvalidInput, entity, fields);
                return foundInvalidInput;
            }
            return false;
        }

        List<Field> fields = ReflectionUtils.getInheritedFields(resource.getParameterizedType());

        for (int i = 0; i < form.size(); i++) {
            Parameter parameter = form.get(i);
            String originalValue = parameter.getValue();


            HtmlPolicyBuilder htmlPolicyBuilder = detectHtmlPolicyBuilder(parameter, fields);

            StringBuilder sb = new StringBuilder();
            // http://stackoverflow.com/questions/12558471/how-to-allow-specific-characters-with-owasp-html-sanitizer
            HtmlSanitizer.Policy policy = createPolicy(htmlPolicyBuilder, sb);
            HtmlSanitizer.sanitize(originalValue, policy);
            String sanitizedHtml = sb.toString();
            if (!sanitizedHtml.equals(originalValue)) {
                log.info(originalValue);
                log.info(sanitizedHtml);
                foundInvalidInput = true;
            }
            parameter.setValue(sanitizedHtml.trim());
        }
        return foundInvalidInput;
    }

    private boolean handleFields(boolean foundInvalidInput, T entity, List<Field> fields) {
        for (Field field : fields) {
            io.skysail.domain.html.Field formField = field.getAnnotation(io.skysail.domain.html.Field.class);
            if (formField == null) {
                continue;
            }
            HtmlPolicy htmlPolicy = formField.htmlPolicy();
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
        return htmlPolicyBuilder.build(HtmlStreamRenderer.create(sb, new Handler<String>() {
            @Override
            public void handle(String x) {
                log.info(this.getClass().getName() + ": " + x);
            }
        }));
    }

    private HtmlPolicyBuilder detectHtmlPolicyBuilder(Parameter parameter, List<Field> fields) {
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
