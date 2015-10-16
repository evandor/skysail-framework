package io.skysail.server.db.it;

import io.skysail.client.testsupport.IntegrationTests;

import org.junit.*;
import org.restlet.*;
import org.restlet.data.MediaType;
import org.restlet.engine.resource.VariantInfo;
import org.restlet.representation.Variant;
import org.restlet.resource.ClientResource;

public class DbTests extends IntegrationTests {

    protected static final Variant HTML_VARIANT = new VariantInfo(MediaType.TEXT_HTML);
    protected static final Variant JSON_VARIANT = new VariantInfo(MediaType.APPLICATION_JSON);

    private ClientResource cr;
    private SkysailItApplication skysailApplication;

    @Before
    public void setUp() throws Exception {
        skysailApplication = SkysailItApplication.getInstance();
    }

    @Test
    public void test() {
        // Bundle[] bundles = thisBundle.getBundleContext().getBundles();
        // for (Bundle bundle : bundles) {
        // System.out.println(bundle.getSymbolicName() + " - " +
        // bundle.getVersion().toString());
        // }
        PostClipResource postClipResource = new PostClipResource();
        // Mockito.doReturn(application).when(resource).getApplication();
        // Mockito.doReturn(query).when(resource).getQuery();

        Request request = new TestRequest();
        // Request request = Mockito.mock(Request.class);
        Response response = new TestResponse(request);
        // responses.put(resource.getClass().getName(), response);
        postClipResource.setApplication(skysailApplication);
        postClipResource.init(null, request, response);
        postClipResource.post(new TestEntity(), HTML_VARIANT);
    }

}
