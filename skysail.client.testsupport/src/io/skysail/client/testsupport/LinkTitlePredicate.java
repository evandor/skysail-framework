package io.skysail.client.testsupport;

import io.skysail.api.links.Link;
import lombok.ToString;

import org.restlet.data.Header;
import org.restlet.util.Series;

@ToString(callSuper = true)
public class LinkTitlePredicate extends LinkPredicate {

    private String title;

    public LinkTitlePredicate(String title, Series<Header> series) {
        super(series);
        this.title = title;
    }

    @Override
    public boolean test(Link l) {
        return l.getTitle().equals(title);
    }

}
