package io.skysail.server.app.designer.entities.resources.test;

import static org.hamcrest.CoreMatchers.*;
import static org.junit.Assert.assertThat;
import io.skysail.api.responses.SkysailResponse;
import io.skysail.server.app.designer.DesignerApplication;
import io.skysail.server.app.designer.application.Application;
import io.skysail.server.app.designer.application.resources.PostApplicationResource;
import io.skysail.server.app.designer.entities.Entity;
import io.skysail.server.app.designer.entities.resources.EntitiesResource;
import io.skysail.server.app.designer.entities.resources.EntityResource;
import io.skysail.server.app.designer.entities.resources.PostEntityResource;
import io.skysail.server.app.designer.entities.resources.PutEntityResource;
import io.skysail.server.app.designer.repo.DesignerRepository;
import io.skysail.server.restlet.resources.SkysailServerResource;
import io.skysail.server.testsupport.ResourceTestBase;

import java.util.HashMap;

import org.apache.shiro.subject.SimplePrincipalMap;
import org.junit.Before;
import org.mockito.*;
import org.restlet.data.Status;

public abstract class AbstractEntityResourceTest extends ResourceTestBase {

    @Spy
    protected PostEntityResource postEntityResource;

    @Spy
    protected PutEntityResource putEntityResource;

    @Spy
    protected EntitiesResource entitiesResource;

    @Spy
    protected EntityResource entityResource;

    protected DesignerRepository repo;

    @Spy
    protected PostApplicationResource postApplicationResource;

    @Before
    public void setUp() throws Exception {
        super.setUpFixture();

        super.setUpApplication(Mockito.mock(DesignerApplication.class));
        super.setUpResource(entityResource);
        super.setUpResource(entitiesResource);
        super.setUpResource(putEntityResource);
        super.setUpResource(postEntityResource);
        super.setUpResource(postApplicationResource);
        setUpRepository(new DesignerRepository());
        setUpSubject("admin");
    }

    protected void assertListResult(SkysailServerResource<?> resource, SkysailResponse<Entity> result, String name) {
        Entity entity = result.getEntity();
        assertThat(responses.get(resource.getClass().getName()).getStatus(),is(equalTo(Status.SUCCESS_CREATED)));
        assertThat(entity.getName(),is(equalTo(name)));
    }

    public void setUpRepository(DesignerRepository todosRepository) {
        repo = todosRepository;
        repo.setDbService(testDb);
        repo.activate();
        ((DesignerApplication)application).setDesignerRepository(repo);
        Mockito.when(((DesignerApplication)application).getRepository()).thenReturn(repo);

    }

    public void setUpSubject(String owner) {
        Mockito.when(subjectUnderTest.getPrincipal()).thenReturn(owner);
        Mockito.when(subjectUnderTest.getPrincipals()).thenReturn(new SimplePrincipalMap(new HashMap<>()));
        setSubject(subjectUnderTest);
    }

    protected Application createApplication() {
//        Application app = new Application();
//        app.setName("Application_" + randomString());
//        SkysailResponse<Application> post = postApplicationResource.post(app,JSON_VARIANT);
//        getAttributes().clear();
//
//        return post.getEntity();
        return null;
    }

    protected Entity createEntity() {
        Entity app = new Entity();
        app.setName("Entity_" + randomString());
        SkysailResponse<Entity> post = postEntityResource.post(app,JSON_VARIANT);
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
