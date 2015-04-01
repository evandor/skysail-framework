package io.skysail.client.testsupport.test;

import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;
import io.skysail.api.links.Link;
import io.skysail.client.testsupport.LinkByExamplePredicate;

import org.junit.Before;
import org.junit.Test;
import org.mockito.Mockito;
import org.restlet.data.Header;
import org.restlet.util.Series;

public class LinkByExamplePredicateTest {

    private Series<Header> header;

    @SuppressWarnings("unchecked")
    @Before
    public void setUp() throws Exception {
        header = Mockito.mock(Series.class);
    }

    @Test
    public void matches_example_link_with_title() {
        Link exampleLink = new Link.Builder("").title("linksTitle").build();
        LinkByExamplePredicate linkByExamplePredicate = new LinkByExamplePredicate(exampleLink, header);
        assertThat(linkByExamplePredicate.test(exampleLink), is(true));
    }
    
    @Test
    public void matches_example_link_with_refId() {
        Link exampleLink = new Link.Builder("").refId("refId").build();
        LinkByExamplePredicate linkByExamplePredicate = new LinkByExamplePredicate(exampleLink, header);
        assertThat(linkByExamplePredicate.test(exampleLink), is(true));
    }
    
    @Test
    public void rejects_link_with_other_title() {
        Link exampleLink = new Link.Builder("").title("linksTitle").build();
        LinkByExamplePredicate linkByExamplePredicate = new LinkByExamplePredicate(exampleLink, header);
        Link otherLink = new Link.Builder("").title("otherTitle").build();
        assertThat(linkByExamplePredicate.test(otherLink), is(false));
    }

    @Test
    public void rejects_link_with_other_refId() {
        Link exampleLink = new Link.Builder("").refId("linksRefId").build();
        LinkByExamplePredicate linkByExamplePredicate = new LinkByExamplePredicate(exampleLink, header);
        Link otherLink = new Link.Builder("").refId("otherRefId").build();
        assertThat(linkByExamplePredicate.test(otherLink), is(false));
    }

}
