package io.skysail.server.restlet.filter;

import io.skysail.api.domain.Identifiable;
import io.skysail.server.restlet.resources.*;
import io.skysail.server.utils.CookiesUtils;

import java.util.*;

import lombok.extern.slf4j.Slf4j;
import de.twenty11.skysail.server.core.restlet.Wrapper;

@Slf4j
public class DataExtractingFilter<R extends SkysailServerResource<?>, T extends Identifiable> extends AbstractResourceFilter<R, T> {

    @Override
    public FilterResult doHandle(R resource, Wrapper responseWrapper) {
        log.debug("entering {}#doHandle", this.getClass().getSimpleName());

        String installation = CookiesUtils.getInstallationFromCookie(resource.getRequest());
        Object entity = resource.getEntity(installation);
        if (entity instanceof List) {
            List<T> data = (List<T>) entity;
            if (data == null && resource instanceof ListServerResource<?>) {
                data = Collections.emptyList();
            }
            sanitizeIds(data);

            responseWrapper.setEntity(data);
            resource.setCurrentEntity(data); // TODO why both wrapper AND resource?
        } else {
            sanitizeIds((T)entity);

            responseWrapper.setEntity(entity);
            resource.setCurrentEntity(entity); // TODO why both wrapper AND resource?

        }
        super.doHandle(resource, responseWrapper);
        return FilterResult.CONTINUE;
    }

    private void sanitizeIds(List<T> data) {
        data.stream().forEach(element -> {
            if (element instanceof Identifiable) {
                replaceHash(element);
            }
        });
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
