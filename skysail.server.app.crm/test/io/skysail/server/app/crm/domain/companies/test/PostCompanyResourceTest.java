package io.skysail.server.app.crm.domain.companies.test;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.CoreMatchers.nullValue;
import static org.hamcrest.Matchers.equalTo;
import static org.junit.Assert.assertThat;
import io.skysail.api.validation.DefaultValidationImpl;
import io.skysail.api.validation.ValidatorService;
import io.skysail.server.app.crm.ContactsGen;
import io.skysail.server.app.crm.domain.companies.CompaniesRepository;
import io.skysail.server.app.crm.domain.companies.Company;
import io.skysail.server.app.crm.domain.companies.PostCompanyResource;
import io.skysail.server.testsupport.AbstractShiroTest;

import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

import javax.validation.Validator;

import org.apache.shiro.subject.Subject;
import org.junit.After;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.runners.MockitoJUnitRunner;
import org.restlet.Request;
import org.restlet.Response;
import org.restlet.data.ClientInfo;
import org.restlet.data.Form;
import org.restlet.data.Reference;

import de.twenty11.skysail.server.core.db.GraphDbService;

@RunWith(MockitoJUnitRunner.class)
public class PostCompanyResourceTest extends AbstractShiroTest {

    @Rule
    public ExpectedException thrown = ExpectedException.none();

    @InjectMocks
    private PostCompanyResource resource;

    @Mock
    private ContactsGen app;

    @Mock
    private CompaniesRepository repo;

    private ConcurrentMap<String, Object> attributes;

    private Form form;

    private Response response;

    @Before
    public void setUp() throws Exception {
        Subject subjectUnderTest = Mockito.mock(Subject.class);
        Mockito.when(subjectUnderTest.isAuthenticated()).thenReturn(true);
        Mockito.when(subjectUnderTest.getPrincipal()).thenReturn("admin");
        setSubject(subjectUnderTest);

        ValidatorService validatorServiceMock = Mockito.mock(ValidatorService.class);
        Validator validator = Mockito.mock(Validator.class);
        Mockito.when(validatorServiceMock.getValidator()).thenReturn(validator);
        GraphDbService dbService = Mockito.mock(GraphDbService.class);
        CompaniesRepository.getInstance().setDbService(dbService);
        Mockito.when(app.getValidatorService()).thenReturn(new DefaultValidationImpl());
        attributes = new ConcurrentHashMap<String, Object>();

        Request request = Mockito.mock(Request.class);
        Mockito.when(request.getClientInfo()).thenReturn(new ClientInfo());
        Mockito.when(request.getAttributes()).thenReturn(attributes);
        Reference resourceRef = Mockito.mock(Reference.class);
        Mockito.when(request.getResourceRef()).thenReturn(resourceRef);
        response = new Response(request);
        resource.init(null, request, response);
        form = Mockito.mock(Form.class);

    }

    @After
    public void tearDownSubject() {
        // 3. Unbind the subject from the current thread:
        clearSubject();
    }

    @Test
    public void creates_registration_template() throws Exception {
        resource.init(null, null, null);
        Company company = resource.createEntityTemplate();
        assertThat(company.getName(), is(nullValue()));
    }

    @Test
    public void empty_html_form_yields_validation_error_code() {
        resource.post(form);
        assertThat(response.getStatus().getCode(), is(400));
        assertThat(response.getHeaders().getFirst("X-Status-Reason").getValue(), is(equalTo("Validation failed")));
        // assertThat(response.getEntityAsText(), is(equalTo("hier")));
    }
}
