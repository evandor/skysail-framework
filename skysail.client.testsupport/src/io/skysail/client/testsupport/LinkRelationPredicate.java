package io.skysail.client.testsupport;

import io.skysail.api.links.Link;
import io.skysail.api.links.LinkRelation;
import lombok.ToString;

import org.restlet.data.Header;
import org.restlet.util.Series;

@ToString(callSuper = true)
public class LinkRelationPredicate extends LinkPredicate {

    private LinkRelation linkRelation;

    public LinkRelationPredicate(LinkRelation linkRelation, Series<Header> series) {
        super(series);
        this.linkRelation = linkRelation;
        
    }

    @Override
    public boolean test(Link l) {
        return l.getRel().equals(linkRelation);
    }
    
}
