package io.skysail.server.app.wiki.pages.resources.test;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.Matchers.lessThan;
import static org.junit.Assert.assertThat;
import io.skysail.api.responses.ConstraintViolationsResponse;
import io.skysail.server.app.wiki.RepositoryHelper;
import io.skysail.server.app.wiki.WikiApplication;
import io.skysail.server.app.wiki.WikiPostOrPutResourceTest;
import io.skysail.server.app.wiki.pages.Page;
import io.skysail.server.app.wiki.pages.resources.PostPageResource;
import io.skysail.server.app.wiki.spaces.Space;
import io.skysail.server.app.wiki.spaces.UniquePerOwnerValidator;
import io.skysail.server.app.wiki.versions.Version;

import java.util.Date;
import java.util.List;

import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;
import org.mockito.Mockito;
import org.mockito.Spy;
import org.restlet.Context;
import org.restlet.data.MediaType;
import org.restlet.data.Status;
import org.restlet.engine.resource.VariantInfo;

public class PostPageResourceTest extends WikiPostOrPutResourceTest {

    private static final long PASSED_TIME_THRESHOLD = 100L;
    @Spy
    private PostPageResource resource;

    @Before
    public void setUp() throws Exception {
        super.setUp(Mockito.mock(WikiApplication.class));
        super.setUp(resource);
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
        resource.post(form, new VariantInfo(MediaType.TEXT_HTML));
        assertThat(response.getStatus(),is(equalTo(Status.SUCCESS_CREATED)));
    }

    @Test
    public void creates_one_version_in_new_page() {
        Space space = postPage("page_" + randomString(), "theContent");
        Space spaceFromDb = repo.getSpaceById(space.getId());
        List<Version> versions = spaceFromDb.getPages().get(0).getVersions();
        assertThat(versions.size(), is(1));
    }

    @Test
    public void creates_content_in_first_version_of_new_page() {
        Space space = postPage("page_" + randomString(), "theContent");
        Space spaceFromDb = repo.getSpaceById(space.getId());
        Version firstVersion = spaceFromDb.getPages().get(0).getVersions().get(0);
        assertThat(firstVersion.getContent(), is(equalTo("theContent")));
    }

    @Test
    public void creates_new_version_in_new_page_belonging_to_current_user() {
        Space space = postPage("page_" + randomString(), "theContent");
        Space spaceFromDb = repo.getSpaceById(space.getId());
        Version firstVersion = spaceFromDb.getPages().get(0).getVersions().get(0);
        assertThat(firstVersion.getOwner(), is(equalTo("admin")));
    }

    @Test
    public void creates_new_version_in_new_page_which_was_created_now() {
        Space space = postPage("page_" + randomString(), "theContent");
        Space spaceFromDb = repo.getSpaceById(space.getId());
        Version firstVersion = spaceFromDb.getPages().get(0).getVersions().get(0);
        assertThat(new Date().getTime() - firstVersion.getCreated().getTime(), is(lessThan(PASSED_TIME_THRESHOLD)));
    }

    private Space postPage(String name, String content) {
        Space space = RepositoryHelper.createTestSpace("admin");
        resource.getRequestAttributes().put("id", space.getId());
        Page newPage = new Page(name);
        newPage.setContent(content);
        resource.init(new Context(), request, response);
        resource.post(newPage, new VariantInfo(MediaType.APPLICATION_JSON));
        assertThat(response.getStatus(),is(equalTo(Status.SUCCESS_CREATED)));
        return space;
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
