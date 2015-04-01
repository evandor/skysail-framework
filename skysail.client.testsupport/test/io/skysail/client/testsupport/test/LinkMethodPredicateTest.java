package io.skysail.client.testsupport.test;

import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;
import io.skysail.api.links.Link;
import io.skysail.client.testsupport.LinkMethodPredicate;

import org.junit.Before;
import org.junit.Test;
import org.mockito.Mockito;
import org.restlet.data.Header;
import org.restlet.data.Method;
import org.restlet.util.Series;

public class LinkMethodPredicateTest {

    private Series<Header> headers;

    @SuppressWarnings("unchecked")
    @Before
    public void setUp() throws Exception {
        headers = Mockito.mock(Series.class);
    }
    
    @Test
    public void accepts_link_with_POST_verb() {
        LinkMethodPredicate predicate = new LinkMethodPredicate(Method.POST, headers);
        Link link = new Link.Builder("uri").verbs(Method.POST).build();
        assertThat(predicate.test(link), is(true));
    }

    @Test
    public void rejects_link_without_POST_verb() {
        LinkMethodPredicate predicate = new LinkMethodPredicate(Method.POST, headers);
        Link link = new Link.Builder("uri").verbs(Method.GET).build();
        assertThat(predicate.test(link), is(false));
    }

}
