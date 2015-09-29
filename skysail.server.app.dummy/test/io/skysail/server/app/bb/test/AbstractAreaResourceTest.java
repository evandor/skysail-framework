//package io.skysail.server.app.bb.test;
//
//import static org.hamcrest.Matchers.is;
//import static org.junit.Assert.assertThat;
//
//import java.util.HashMap;
//
//import org.apache.shiro.subject.SimplePrincipalMap;
//import org.junit.Before;
//import org.mockito.Mockito;
//import org.mockito.Spy;
//import org.restlet.data.Status;
//
//import io.skysail.api.responses.SkysailResponse;
//import io.skysail.server.app.bb.AreaOld;
//import io.skysail.server.app.bb.AreasResource;
//import io.skysail.server.app.bb.BBRepository;
//import io.skysail.server.app.bb.BBApplication;
//import io.skysail.server.app.bb.areas.PostAreaResource;
//import io.skysail.server.repo.Repository;
//import io.skysail.server.restlet.resources.SkysailServerResource;
//import io.skysail.server.testsupport.ResourceTestBase;
//
//public abstract class AbstractAreaResourceTest extends ResourceTestBase {
//
//    @Spy
//    protected PostAreaResource postAreaResource;
//
////    @Spy
////    protected PutListResource putListResource;
//
//    @Spy
//    protected AreasResource listsResource;
//
////    @Spy
////    protected ListResource listResource;
//
//    @Spy
//    private BBApplication application;
//
//    protected BBRepository repo;
//
//    @Before
//    public void setUp() throws Exception {
//        super.setUpFixture();
//
//        super.setUpApplication(application);//Mockito.mock(TodoApplication.class));
////        super.setUpResource(listResource);
//        super.setUpResource(listsResource);
////        super.setUpResource(putListResource);
//        super.setUpResource(postAreaResource);
//        setUpRepository(new BBRepository());
//        setUpSubject("admin");
//
////        application.setRe(testDb);
//
//        //new UniquePerOwnerValidator().setDbService(testDb);
//    }
//
//    protected void assertListResult(SkysailServerResource<?> resource, SkysailResponse<AreaOld> result, String title) {
//        AreaOld entity = result.getEntity();
//        assertThat(responses.get(resource.getClass().getName()).getStatus(),is(Status.SUCCESS_CREATED));
//        assertThat(entity.getTitle(),is(title));
//    }
//
//    public void setUpRepository(BBRepository rep) {
//        repo = rep;
//        repo.setDbService(testDb);
//        repo.activate();
//       // ((BodyboosterApplication)application).setRepository(repo);
//        Mockito.when(((BBApplication)application).getRepository()).thenReturn(repo);
//
//    }
//
//    public void setUpSubject(String owner) {
//        Mockito.when(subjectUnderTest.getPrincipal()).thenReturn(owner);
//        Mockito.when(subjectUnderTest.getPrincipals()).thenReturn(new SimplePrincipalMap(new HashMap<>()));
//        setSubject(subjectUnderTest);
//    }
//
////    protected TodoList createList() {
////        TodoList aList = new TodoList();
////        aList.setName("list_" + randomString());
////        SkysailResponse<TodoList> post = postListresource.post(aList,JSON_VARIANT);
////        getAttributes().clear();
////
////        return post.getEntity();
////    }
//
//    protected void init(SkysailServerResource<?> resource) {
//        resource.init(null, request, responses.get(resource.getClass().getName()));
//    }
//
//    protected void setAttributes(String name, String id) {
//        getAttributes().clear();
//        getAttributes().put(name, id);
//    }
//
//}
