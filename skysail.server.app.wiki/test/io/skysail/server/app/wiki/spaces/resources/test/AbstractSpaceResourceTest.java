package io.skysail.server.app.wiki.spaces.resources.test;

import static org.hamcrest.CoreMatchers.*;
import static org.junit.Assert.assertThat;
import io.skysail.api.responses.SkysailResponse;
import io.skysail.server.app.wiki.WikiApplication;
import io.skysail.server.app.wiki.repository.*;
import io.skysail.server.app.wiki.spaces.*;
import io.skysail.server.app.wiki.spaces.resources.*;
import io.skysail.server.restlet.resources.SkysailServerResource;
import io.skysail.server.testsupport.ResourceTestBase;

import java.util.HashMap;

import org.apache.shiro.subject.SimplePrincipalMap;
import org.junit.Before;
import org.mockito.*;
import org.restlet.data.Status;

public abstract class AbstractSpaceResourceTest  extends ResourceTestBase {

    @Spy
    protected PostSpaceResource postSpaceResource;

    @Spy
    protected PutSpaceResource putSpaceResource;

    @Spy
    protected SpacesResource spacesResource;

    @Spy
    protected SpaceResource spaceResource;

    @Spy
    private WikiApplication application;

    protected PagesRepository pagesRepo;
    protected SpacesRepository spacesRepo;

    @Before
    public void setUp() throws Exception {
        super.setUpFixture();

        super.setUpApplication(application);//Mockito.mock(TodoApplication.class));
        super.setUpResource(spaceResource);
        super.setUpResource(spacesResource);
        super.setUpResource(putSpaceResource);
        super.setUpResource(postSpaceResource);
        setUpSpacesRepository(new SpacesRepository());
        setUpPagesRepository(new PagesRepository());
        setUpSubject("admin");

        new UniquePerOwnerValidator().setDbService(testDb);
    }

    protected void assertSpaceResult(SkysailServerResource<?> resource, SkysailResponse<Space> result, String name) {
        Space entity = result.getEntity();
        assertThat(responses.get(resource.getClass().getName()).getStatus(),is(equalTo(Status.SUCCESS_CREATED)));
        assertThat(entity.getName(),is(equalTo(name)));
//        assertThat(entity.getCreated(),is(not(nullValue())));
//        assertThat(entity.getModified(),is(nullValue()));
        assertThat(entity.getOwner(),is("admin"));
    }

    public void setUpSpacesRepository(SpacesRepository repo) {
        this.spacesRepo = repo;
        spacesRepo.setDbService(testDb);
        spacesRepo.activate();
        ((WikiApplication)application).setSpacesRepository(spacesRepo);
        Mockito.when(((WikiApplication)application).getSpacesRepo()).thenReturn(spacesRepo);

    }

    public void setUpPagesRepository(PagesRepository repo) {
        this.pagesRepo = repo;
        pagesRepo.setDbService(testDb);
        pagesRepo.activate();
        ((WikiApplication)application).setPagesRepository(pagesRepo);
        Mockito.when(((WikiApplication)application).getPagesRepo()).thenReturn(pagesRepo);

    }

    public void setUpSubject(String owner) {
        Mockito.when(subjectUnderTest.getPrincipal()).thenReturn(owner);
        Mockito.when(subjectUnderTest.getPrincipals()).thenReturn(new SimplePrincipalMap(new HashMap<>()));
        setSubject(subjectUnderTest);
    }

    protected Space createSpace() {
        Space aSpace = new Space();
        aSpace.setName("list_" + randomString().substring(0,15));
        SkysailResponse<Space> post = postSpaceResource.post(aSpace,JSON_VARIANT);
        getAttributes().clear();

        return post.getEntity();
    }

    protected void init(SkysailServerResource<?> resource) {
        resource.init(null, request, responses.get(resource.getClass().getName()));
    }

    protected void setAttributes(String name, String id) {
        getAttributes().clear();
        getAttributes().put(name, id);
    }


}
