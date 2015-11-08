//package io.skysail.server.app.todos.services.test;
//
//import static org.mockito.Mockito.*;
//import io.skysail.server.app.todos.TodoApplication;
//import io.skysail.server.app.todos.repo.*;
//import io.skysail.server.app.todos.services.*;
//import io.skysail.server.app.todos.todos.resources.TodosResource;
//import io.skysail.server.testsupport.AbstractShiroTest;
//
//import org.apache.shiro.subject.Subject;
//import org.junit.*;
//import org.junit.runner.RunWith;
//import org.mockito.*;
//import org.mockito.runners.MockitoJUnitRunner;
//
//@RunWith(MockitoJUnitRunner.class)
//public class TodosServiceTest extends AbstractShiroTest {
//
//    @Mock
//    private ListsRepository listRepo;
//
//    @Mock
//    private TodosRepository todoRepo;
//
//    @Mock
//    private TodosResource listsResource;
//
//    @InjectMocks
//    private ListService listService;
//    private TodoApplication application;
//
//    private TodosService todosService;
//
//    @Before
//    public void setUp() {
//        super.setUp();
//        subjectUnderTest = Mockito.mock(Subject.class);
//        setSubject(subjectUnderTest);
//        application = mock(TodoApplication.class);
//
//        listService = new ListService(listRepo);
//        todosService = new TodosService(listRepo);
//
//        when(subjectUnderTest.getPrincipal()).thenReturn("principal");
//        when(listsResource.getResponse()).thenReturn(response);
//        when(listsResource.getRequest()).thenReturn(request);
//        when(listsResource.getApplication()).thenReturn(application);
//        when(application.getListRepo()).thenReturn(listRepo);
//        when(application.getTodosRepo()).thenReturn(todoRepo);
//    }
//
//    @After
//    public void tearDownSubject() {
//        clearSubject();
//    }
//
//    @Test
//    public void getTodo_delegates_to_repository_find_one() {
////        todosService.getTodo(listsResource, "#12:0");
////        verify(listRepo).findOne("#12:0");
//    }
//
//}