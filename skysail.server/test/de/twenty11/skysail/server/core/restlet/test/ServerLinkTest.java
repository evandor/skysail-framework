package de.twenty11.skysail.server.core.restlet.test;

import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.is;
import static org.junit.Assert.assertThat;

import java.util.Arrays;

import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Ignore;
import org.junit.Test;
import org.mockito.Mockito;

import de.twenty11.skysail.api.responses.LinkHeaderRelation;
import de.twenty11.skysail.api.responses.Linkheader;
import de.twenty11.skysail.server.app.SkysailApplication;
import de.twenty11.skysail.server.core.restlet.ServerLink;
import de.twenty11.skysail.server.core.restlet.SkysailServerResource;

public class ServerLinkTest {

    @BeforeClass
    public static void setUpBeforeClass() throws Exception {
    }

    public class SsrClass extends SkysailServerResource<String> {
    	
        @Override
        public String getData() {
            return "data";
        }
        
        @Override
        public LinkHeaderRelation getLinkRelation() {
            return LinkHeaderRelation.COLLECTION;
        }

    }

    private SkysailApplication app;

    @Before
    public void setUp() throws Exception {
        app = Mockito.mock(SkysailApplication.class);
    }
    
    @Test
    @Ignore
    public void testName() throws Exception {
        Mockito.when(app.getTemplatePaths(SsrClass.class)).thenReturn(Arrays.asList("/path"));
        Linkheader serverLink = ServerLink.fromResource(app, SsrClass.class, "text");
        assertThat(serverLink.getTitle(), is(equalTo("text")));
        assertThat(serverLink.getUri(), is(equalTo("/path")));
    }

}
