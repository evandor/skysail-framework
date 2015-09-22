//package io.skysail.server.app.todos.todos.test;
//
//import static org.hamcrest.CoreMatchers.equalTo;
//import static org.hamcrest.CoreMatchers.is;
//import static org.junit.Assert.assertThat;
//import io.skysail.api.validation.DefaultValidationImpl;
//import io.skysail.server.app.todos.TodoApplication;
//import io.skysail.server.app.todos.repo.TodosRepository;
//import io.skysail.server.db.DbService;
//import io.skysail.server.testsupport.AbstractShiroTest;
//
//import java.util.Locale;
//
//import org.junit.After;
//import org.junit.Rule;
//import org.junit.rules.ExpectedException;
//import org.mockito.Mockito;
//import org.mockito.Spy;
//import org.restlet.data.Form;
//
//public class TodoAppTest extends AbstractShiroTest {
//
//    @Rule
//    public ExpectedException thrown = ExpectedException.none();
//
//    @Spy
//    protected TodoApplication app;
//
//    protected TodosRepository todoRepo;
//
//    protected DbService dbService;
//
//    public void setUp() throws Exception {
//        super.setUp();
//        todoRepo = new TodosRepository();
//        dbService = new InMemoryDbService();
//        todoRepo.setDbService(dbService);
//        app.setRepository(todoRepo);
//        Mockito.when(app.getValidatorService()).thenReturn(new DefaultValidationImpl());
//
//        form = new AForm();
//        Locale locale_en = new Locale("en");
//        Locale.setDefault(locale_en);
//    }
//
//    @After
//    public void tearDownSubject() {
//        clearSubject();
//    }
//
//    protected void assertValidationFailed(int statusCode, String xStatusReason) {
//        assertThat(response.getStatus().getCode(), is(statusCode));
//        assertThat(response.getHeaders().getFirst("X-Status-Reason").getValue(), is(equalTo(xStatusReason)));
//    }
//
//}
