package io.skysail.client.testsupport;

import io.skysail.api.links.Link;

import java.util.function.Predicate;

import lombok.Getter;

import org.restlet.data.Header;
import org.restlet.util.Series;

@Getter
public abstract class LinkPredicate implements Predicate<Link> {

    protected String link;

    public LinkPredicate(Series<Header> series) {
        this.link = series.getFirstValue("Link");
    }

    @Override
    public abstract boolean test(Link l);

    @Override
    public String toString() {
        return "\n - " + link.replace(",", "\n - ");
    }
}