package io.skysail.server.restlet.filter;

import io.skysail.api.domain.Identifiable;
import io.skysail.server.restlet.resources.*;

import java.util.*;

import lombok.extern.slf4j.Slf4j;
import de.twenty11.skysail.server.core.restlet.ResponseWrapper;
import de.twenty11.skysail.server.core.restlet.utils.CookiesUtils;

@Slf4j
public class DataExtractingFilter<R extends SkysailServerResource<T>, T> extends AbstractResourceFilter<R, T> {

    @Override
    public FilterResult doHandle(R resource, ResponseWrapper<T> responseWrapper) {
        log.debug("entering {}#doHandle", this.getClass().getSimpleName());

        String installation = CookiesUtils.getInstallationFromCookie(resource.getRequest());
        T data = resource.getEntity(installation);
        if (data == null && resource instanceof ListServerResource<?>) {
            data = (T) Collections.emptyList();
        }
        sanitizeIds(data);
        
        responseWrapper.setEntity(data);
        resource.setCurrentEntity(data);
        super.doHandle(resource, responseWrapper);
        return FilterResult.CONTINUE;
    }

    private void sanitizeIds(T data) {
        if (data instanceof List) {
            ((List<?>) data).stream().forEach(element -> {
                if (element instanceof Identifiable) {
                    replaceHash(element);
                }
            });
        } else if (data instanceof Identifiable) {
            replaceHash(data);
        }
    }

    private void replaceHash(Object element) {
        Identifiable identifiable = (Identifiable) element;
        if (identifiable.getId() != null) {
            identifiable.setId(identifiable.getId().replace("#", ""));
        }
    }

}
