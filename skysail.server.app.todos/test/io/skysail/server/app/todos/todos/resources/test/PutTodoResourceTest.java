package io.skysail.server.app.todos.todos.resources.test;

import io.skysail.api.validation.ValidatorService;
import io.skysail.server.app.todos.TodoApplication;
import io.skysail.server.app.todos.todos.resources.PutTodoResource;
import io.skysail.server.testsupport.PutResourceTest;

import java.util.concurrent.atomic.AtomicReference;

import javax.validation.Validator;

import org.junit.*;
import org.mockito.*;

public class PutTodoResourceTest extends PutResourceTest {

    @Spy
    private PutTodoResource resource;

    @Mock
    private TodoApplication application;

    @Override
    @Before
    public void setUp() throws Exception {
        super.setUp();

        Mockito.doReturn(application).when(resource).getApplication();
        AtomicReference<ValidatorService> validatorServiceRef = new AtomicReference<>();
        ValidatorService validatorService = Mockito.mock(ValidatorService.class);
        Validator validator = Mockito.mock(Validator.class);
        validatorServiceRef.set(validatorService);
        Mockito.doReturn(validator).when(validatorService).getValidator();
        Mockito.doReturn(validatorServiceRef).when(application).getValidatorService();
        Mockito.doReturn(query).when(resource).getQuery();

        resource.init(null, request, response);
    }

    @Test
    public void rejects_updating_password_if_old_password_is_null() throws Exception {
        Mockito.when(subjectUnderTest.getPrincipal()).thenReturn("admin");
        setSubject(subjectUnderTest);
//        resource.put(form, null);
//        assertThat(response.getStatus().getCode(), is(400));
    }
}
