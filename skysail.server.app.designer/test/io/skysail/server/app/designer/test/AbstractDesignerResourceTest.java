package io.skysail.server.app.designer.test;

import java.util.HashMap;

import org.apache.shiro.subject.SimplePrincipalMap;
import org.junit.Before;
import org.mockito.*;
import org.restlet.Context;

import io.skysail.api.responses.SkysailResponse;
import io.skysail.server.app.designer.DesignerApplication;
import io.skysail.server.app.designer.application.DbApplication;
import io.skysail.server.app.designer.application.resources.*;
import io.skysail.server.app.designer.entities.DbEntity;
import io.skysail.server.app.designer.entities.resources.*;
import io.skysail.server.app.designer.repo.DesignerRepository;
import io.skysail.server.restlet.resources.SkysailServerResource;
import io.skysail.server.testsupport.ResourceTestBase;

public abstract class AbstractDesignerResourceTest extends ResourceTestBase {

    @Spy
    protected PostApplicationResource postApplicationResource;

    @Spy
    protected PutApplicationResource putApplicationResource;

    @Spy
    protected ApplicationsResource applicationsResource;

    @Spy
    protected ApplicationResource applicationResource;

    @Spy
    protected PostEntityResource postEntityResource;

    @Spy
    protected PutEntityResource putEntityResource;

    @Spy
    protected EntitiesResource entitiesResource;

    @Spy
    protected EntityResource entityResource;

    @Spy
    protected DesignerApplication application;
    
    protected DesignerRepository repo;

    @Before
    public void setUp() throws Exception {
        super.setUpFixture();

        Context context = super.setUpApplication(application);
        
        setUpRepository(new DesignerRepository());
        
        super.setUpResource(applicationResource, context);
        super.setUpResource(applicationsResource, context);
        super.setUpResource(putApplicationResource, context);
        super.setUpResource(postApplicationResource, context);
        super.setUpResource(entityResource, context);
        super.setUpResource(entitiesResource, context);
        super.setUpResource(putEntityResource, context);
        super.setUpResource(postEntityResource, context);
        
        setUpSubject("admin");
    }

    public void setUpRepository(DesignerRepository designerRepository) {
        repo = designerRepository;
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

    protected void init(SkysailServerResource<?> resource) {
        resource.init(null, request, responses.get(resource.getClass().getName()));
    }

    protected void setAttributes(String name, String id) {
        getAttributes().clear();
        getAttributes().put(name, id);
    }

    protected DbApplication createValidApplication() {
        DbApplication app = DbApplication.builder()
                .name("app_name_" + randomString())
                .packageName("app_packageName_" + randomString())
                .path("../")
                .projectName("projectName")
                .build();
//        DbApplication app = new DbApplication();
//        app.setName("application_" + randomString());
//        app.setPackageName("package");
//        app.setPath("../");
//        app.setProjectName("projectName");
        SkysailResponse<DbApplication> post = postApplicationResource.post(app,JSON_VARIANT);
        getAttributes().clear();

        return post.getEntity();
    }

    protected DbEntity createEntity() {
        DbEntity app = new DbEntity();
        app.setName("Entity_" + randomString());
        SkysailResponse<DbEntity> post = postEntityResource.post(app,JSON_VARIANT);
        getAttributes().clear();

        return post.getEntity();
    }




}
