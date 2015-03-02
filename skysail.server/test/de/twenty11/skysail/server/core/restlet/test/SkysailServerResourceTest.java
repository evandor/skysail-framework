package de.twenty11.skysail.server.core.restlet.test;

import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.is;
import static org.junit.Assert.assertThat;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.runners.MockitoJUnitRunner;

import de.twenty11.skysail.api.responses.LinkHeaderRelation;
import de.twenty11.skysail.server.core.restlet.SkysailServerResource;

@RunWith(MockitoJUnitRunner.class)
public class SkysailServerResourceTest {

    private TestSkysailServerResource serverResource;

    private class TestSkysailServerResource extends SkysailServerResource<String> {

        @Override
        public LinkHeaderRelation getLinkRelation() {
            return LinkHeaderRelation.ABOUT;
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
    public void returns_resources_data() throws Exception {
        assertThat(serverResource.getEntity(), is(equalTo(TestSkysailServerResource.class.getSimpleName())));
    }

    @Test
    public void returns_resources_linkRelation() throws Exception {
        assertThat(serverResource.getLinkRelation(), is(equalTo(LinkHeaderRelation.ABOUT)));
    }

    @Test
    public void testName() throws Exception {
        assertThat(serverResource.getEntityType(), is(equalTo("java.lang.String")));
    }

}
