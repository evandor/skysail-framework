package io.skysail.server.restlet.filter;

import io.skysail.server.restlet.resources.SkysailServerResource;

import java.util.Iterator;
import java.util.NoSuchElementException;
import java.util.Set;

import de.twenty11.skysail.server.app.SkysailApplication;

/**
 * Idea: let custom GET requests add custom pre-Filters. For now: fixed metrics
 * filter
 *
 * @param <R>
 * @param <T>
 */
public class GetRequestPreFilterHook<R extends SkysailServerResource<T>, T> extends AbstractResourceFilter<R, T> {

    public GetRequestPreFilterHook(SkysailApplication application) {
        if (application == null) {
            return;
        }
        Set<HookFilter> filters = application.getFilters();
        if (filters == null) {
            return;
        }
        Iterator<HookFilter> iterator = filters.iterator();
        if (iterator == null || !iterator.hasNext()) {
            return;
        }
        AbstractResourceFilter<R, T> first = iterator.next().getFilter();
        AbstractResourceFilter<R, T> current = first;
        while(iterator.hasNext()) {
            AbstractResourceFilter<R, T> next = iterator.next().getFilter();
            current.setNext(next);
            current = next;
        }
        this.setNext(first);
    }
}
