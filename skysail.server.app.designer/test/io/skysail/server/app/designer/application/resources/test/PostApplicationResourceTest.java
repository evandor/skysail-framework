package io.skysail.server.app.designer.application.resources.test;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;
import io.skysail.api.responses.ConstraintViolationsResponse;
import io.skysail.server.app.designer.DesignerApplication;
import io.skysail.server.app.designer.application.resources.PostApplicationResource;
import io.skysail.server.app.designer.repo.DesignerRepository;
import io.skysail.server.testsupport.PostResourceTest;

import java.util.HashMap;

import org.apache.shiro.subject.SimplePrincipalMap;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mockito;
import org.mockito.Spy;
import org.restlet.data.MediaType;
import org.restlet.data.Status;
import org.restlet.engine.resource.VariantInfo;

public class PostApplicationResourceTest extends PostResourceTest {

   
    @Spy
    private PostApplicationResource resource;

    @Before
    public void setUp() throws Exception {
        super.setUp(Mockito.mock(DesignerApplication.class), resource);

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
        assertThat(response.getStatus(),is(equalTo(Status.CLIENT_ERROR_BAD_REQUEST)));
        assertThat(response.getHeaders().getFirst("X-Status-Reason").getValue(),is(equalTo("Validation failed")));
        assertThat(post.getViolations().size(),is(1));
    }
    
    @Test
    public void valid_data_yields_new_entity() {
        form.add("name", "application1");
        resource.post(form, new VariantInfo(MediaType.TEXT_HTML));
        assertThat(response.getStatus(),is(equalTo(Status.SUCCESS_CREATED)));
    }


    
}
