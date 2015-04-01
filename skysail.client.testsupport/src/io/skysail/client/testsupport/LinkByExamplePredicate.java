package io.skysail.client.testsupport;

import io.skysail.api.links.Link;
import lombok.ToString;

import org.restlet.data.Header;
import org.restlet.util.Series;

@ToString(callSuper = true)
public class LinkByExamplePredicate extends LinkPredicate {

    private Link exampleLink;

    public LinkByExamplePredicate(Link exampleLink, Series<Header> series) {
        super(series);
        this.exampleLink = exampleLink;
    }

    @Override
    public boolean test(Link l) {
        String title = exampleLink.getTitle();
        if (title != null && (!(l.getTitle().equals(title)))) {
                return false;
        }
        String refId = exampleLink.getRefId();
        if (refId != null && (!(l.getRefId().equals(refId)))) {
                return false;
        }
        return true;
    }
}
