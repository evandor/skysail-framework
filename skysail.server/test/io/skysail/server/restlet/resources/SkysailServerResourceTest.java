package io.skysail.server.restlet.resources;

import static org.hamcrest.Matchers.*;
import static org.junit.Assert.assertThat;
import io.skysail.api.links.LinkRelation;

import org.junit.*;
import org.junit.runner.RunWith;
import org.mockito.runners.MockitoJUnitRunner;

@RunWith(MockitoJUnitRunner.class)
public class SkysailServerResourceTest {

    private TestSkysailServerResource serverResource;

    private class TestSkysailServerResource extends SkysailServerResource<String> {

        @Override
        public LinkRelation getLinkRelation() {
            return LinkRelation.ABOUT;
        }

        @Override
        public String getEntity() {
            return null;
        }

    };

    @Before
    public void setUp() throws Exception {
        serverResource = new TestSkysailServerResource();
    }

    @Test
    public void returns_resources_linkRelation() throws Exception {
        assertThat(serverResource.getLinkRelation(), is(equalTo(LinkRelation.ABOUT)));
    }

    @Test
    public void testName() throws Exception {
        assertThat(serverResource.getEntityType(), is(equalTo("java.lang.String")));
    }

}
