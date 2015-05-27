package io.skysail.server.app.wiki.spaces.resources.test;

import static org.hamcrest.CoreMatchers.*;
import static org.junit.Assert.assertThat;
import io.skysail.api.responses.ConstraintViolationsResponse;
import io.skysail.server.app.wiki.WikiApplication;
import io.skysail.server.app.wiki.repository.WikiRepository;
import io.skysail.server.app.wiki.spaces.*;
import io.skysail.server.app.wiki.spaces.resources.PostSpaceResource;
import io.skysail.server.testsupport.PostResourceTest;

import java.util.HashMap;

import org.apache.shiro.subject.SimplePrincipalMap;
import org.junit.*;
import org.mockito.*;
import org.restlet.data.*;
import org.restlet.engine.resource.VariantInfo;

public class PostSpaceResourceTest extends PostResourceTest {

    @Spy
    private PostSpaceResource resource;

    @Before
    public void setUp() throws Exception {
        super.setUp(Mockito.mock(WikiApplication.class), resource);

        WikiRepository repo = new WikiRepository();
        repo.setDbService(testDb);
        repo.activate();
        ((WikiApplication)application).setWikiRepository(repo);
        
        Mockito.when(subjectUnderTest.getPrincipal()).thenReturn("admin");
        Mockito.when(subjectUnderTest.getPrincipals()).thenReturn(new SimplePrincipalMap(new HashMap<>()));
        setSubject(subjectUnderTest);
        
        new UniquePerOwnerValidator().setDbService(testDb);
    }

    @Test
    public void empty_html_form_yields_validation_failure() {
        ConstraintViolationsResponse<?> post = (ConstraintViolationsResponse<?>) resource.post(form, new VariantInfo(MediaType.TEXT_HTML));
        assertValidationFailure(post,  "name", "may not be null");
    }

    @Test
    public void empty_json_request_yields_validation_failure() {
        ConstraintViolationsResponse<?> post = (ConstraintViolationsResponse<?>) resource.post(new Space());
        assertValidationFailure(post, "name", "may not be null");
    }

    @Test
    public void valid_form_data_yields_new_entity() {
        form.add("name", "space_" + randomString());
        resource.post(form, new VariantInfo(MediaType.TEXT_HTML));
        assertThat(response.getStatus(),is(equalTo(Status.SUCCESS_CREATED)));
    }

    @Test
    public void valid_data_yields_new_entity() {
        Space newTodoList = new Space("space_" + randomString());
        resource.post(newTodoList);
        assertThat(response.getStatus(),is(equalTo(Status.SUCCESS_CREATED)));
    }

    @Test
    public void two_entries_with_same_name_yields_failure() {
        form.add("name", "list_" + randomString());
        resource.post(form, new VariantInfo(MediaType.TEXT_HTML));
        ConstraintViolationsResponse<?> post = (ConstraintViolationsResponse<?>) resource.post(form, new VariantInfo(MediaType.TEXT_HTML));
        assertValidationFailure(post,  "", "name already exists");
    }


}