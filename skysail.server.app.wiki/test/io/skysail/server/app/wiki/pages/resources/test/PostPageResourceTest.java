package io.skysail.server.app.wiki.pages.resources.test;

import static org.hamcrest.CoreMatchers.*;
import static org.junit.Assert.assertThat;
import io.skysail.api.responses.*;
import io.skysail.server.app.wiki.*;
import io.skysail.server.app.wiki.pages.Page;
import io.skysail.server.app.wiki.pages.resources.PostPageResource;
import io.skysail.server.app.wiki.spaces.*;

import org.junit.*;
import org.mockito.*;
import org.restlet.Context;
import org.restlet.data.*;
import org.restlet.engine.resource.VariantInfo;

public class PostPageResourceTest extends WikiPostOrPutResourceTest {

    @Spy
    private PostPageResource resource;

    @Before
    public void setUp() throws Exception {
        super.setUp(Mockito.mock(WikiApplication.class), resource);
        initRepository();
        initUser("admin");
        new UniquePerOwnerValidator().setDbService(testDb);
    }

    @Test
    public void empty_html_form_yields_validation_failure() {
        resource.init(null, request, response);
        ConstraintViolationsResponse<?> post = (ConstraintViolationsResponse<?>) resource.post(form, new VariantInfo(MediaType.TEXT_HTML));
        assertValidationFailure(post,  "name", "may not be null");
    }

    @Test
    public void empty_json_request_yields_validation_failure() {
        resource.init(null, request, response);
        ConstraintViolationsResponse<?> post = (ConstraintViolationsResponse<?>) resource.post(new Page(), new VariantInfo(MediaType.APPLICATION_JSON));
        assertValidationFailure(post, "name", "may not be null");
    }

    @Test
    public void valid_form_data_yields_new_entity() {
        Space space = RepositoryHelper.createTestSpace("admin");
        resource.getRequestAttributes().put("id", space.getId());
        form.add("name", "page_" + randomString());
        
        resource.init(new Context(), request, response);
        SkysailResponse<Page> reponse = resource.post(form, new VariantInfo(MediaType.TEXT_HTML));
        
        System.out.println(response);
        
        assertThat(response.getStatus(),is(equalTo(Status.SUCCESS_CREATED)));
    }

    @Test
    public void valid_data_yields_new_entity() {
        Space space = RepositoryHelper.createTestSpace("admin");
        resource.getRequestAttributes().put("id", space.getId());
        Page newTodoList = new Page("page_" + randomString());
        resource.init(new Context(), request, response);
        resource.post(newTodoList, new VariantInfo(MediaType.APPLICATION_JSON));
        assertThat(response.getStatus(),is(equalTo(Status.SUCCESS_CREATED)));
    }

    @Test
    @Ignore
    public void two_entries_with_same_name_yields_failure() {
        form.add("name", "space_" + randomString());
        resource.post(form, new VariantInfo(MediaType.TEXT_HTML));
        ConstraintViolationsResponse<?> post = (ConstraintViolationsResponse<?>) resource.post(form, new VariantInfo(MediaType.TEXT_HTML));
        assertValidationFailure(post,  "", "name already exists");
    }


}
