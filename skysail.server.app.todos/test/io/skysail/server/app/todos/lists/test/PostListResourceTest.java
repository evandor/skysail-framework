package io.skysail.server.app.todos.lists.test;

import io.skysail.api.validation.ValidatorService;
import io.skysail.server.app.todos.TodoApplication;
import io.skysail.server.app.todos.lists.PostListResource;
import io.skysail.server.app.todos.repo.TodosRepository;
import io.skysail.server.testsupport.PostResourceTest;

import java.util.*;
import java.util.concurrent.atomic.AtomicReference;

import javax.validation.Validator;

import org.apache.shiro.subject.*;
import org.junit.*;
import org.mockito.*;
import org.restlet.data.MediaType;
import org.restlet.engine.resource.VariantInfo;

import de.twenty11.skysail.server.services.EncryptorService;

public class PostListResourceTest extends PostResourceTest {

    @Spy
    private PostListResource resource;

    @Mock
    private TodoApplication application;

    @Override
    @Before
    public void setUp() throws Exception {
        super.setUp();

        Mockito.doReturn(application).when(resource).getApplication();
        AtomicReference<ValidatorService> validatorServiceRef = new AtomicReference<>();
        AtomicReference<EncryptorService> encryptorServiceRef = new AtomicReference<>();
        ValidatorService validatorService = Mockito.mock(ValidatorService.class);
        Validator validator = Mockito.mock(Validator.class);
        validatorServiceRef.set(validatorService);
        Mockito.doReturn(validator).when(validatorService).getValidator();
        Mockito.doReturn(validatorServiceRef).when(application).getValidatorService();
        Mockito.doReturn(encryptorServiceRef).when(application).getEncryptorService();
        Mockito.doReturn(query).when(resource).getQuery();
        
        TodosRepository repo = new TodosRepository();
//        OrientGraphDbService dbService = new OrientGraphDbService();
        repo.setDbService(testDb);
        application.setRepository(repo);

        resource.init(null, request, response);
    }

    @Test
    public void testName() {
        Mockito.when(subjectUnderTest.getPrincipal()).thenReturn("admin");

        Map<String, Map<String, Object>> backingMap = new HashMap<>();
        backingMap.put("admin", new HashMap<String, Object>() {{
            put("a","B");
        }});
        PrincipalCollection principalCollection = new SimplePrincipalMap(backingMap);
        Mockito.when(subjectUnderTest.getPrincipals()).thenReturn(principalCollection);

        setSubject(subjectUnderTest);

        Object post = resource.post(form, new VariantInfo(MediaType.TEXT_HTML));
        System.out.println(post);
    }
}
