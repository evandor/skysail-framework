package io.skysail.server.app.designer.application.resources.test;

import static org.hamcrest.CoreMatchers.*;
import static org.junit.Assert.assertThat;
import io.skysail.api.responses.ConstraintViolationsResponse;
import io.skysail.server.app.designer.DesignerApplication;
import io.skysail.server.app.designer.application.resources.PostApplicationResource;
import io.skysail.server.app.designer.repo.DesignerRepository;
import io.skysail.server.testsupport.ResourceTestBase;

import java.util.HashMap;

import org.apache.shiro.subject.SimplePrincipalMap;
import org.junit.*;
import org.mockito.*;
import org.restlet.data.*;
import org.restlet.engine.resource.VariantInfo;

public class PostApplicationResourceTest extends ResourceTestBase {


    @Spy
    private PostApplicationResource resource;

    @Before
    public void setUp() throws Exception {
        super.setUpFixture();
        super.setUpApplication(Mockito.mock(DesignerApplication.class));
        super.setUpResource(resource);

        DesignerRepository repo = new DesignerRepository();
        repo.setDbService(testDb);
        repo.activate();
        ((DesignerApplication)application).setDesignerRepository(repo);

        Mockito.when(subjectUnderTest.getPrincipal()).thenReturn("admin");
        Mockito.when(subjectUnderTest.getPrincipals()).thenReturn(new SimplePrincipalMap(new HashMap<>()));
        setSubject(subjectUnderTest);
    }

    @Test
    public void empty_form_yields_validation_failure() {
        ConstraintViolationsResponse<?> post = (ConstraintViolationsResponse<?>) resource.post(form, new VariantInfo(MediaType.TEXT_HTML));
        assertThat(responses.get(resource.getClass().getName()).getStatus(),is(equalTo(Status.CLIENT_ERROR_BAD_REQUEST)));
        assertThat(responses.get(resource.getClass().getName()).getHeaders().getFirst("X-Status-Reason").getValue(),is(equalTo("Validation failed")));
        assertThat(post.getViolations().size(),is(4));
    }

    @Test
//    @Ignore // TODO
    public void valid_data_yields_new_entity() {
        form.add("name", "application1");
        form.add("path", "../");
        form.add("packageName", "io.skysail.app.test");
        form.add("projectName", "testproj");
        resource.post(form, new VariantInfo(MediaType.TEXT_HTML));
        assertThat(responses.get(resource.getClass().getName()).getStatus(),is(equalTo(Status.SUCCESS_CREATED)));
    }



}
