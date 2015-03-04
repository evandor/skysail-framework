package io.skysail.server.app.crm.domain.contacts.test;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.CoreMatchers.nullValue;
import static org.junit.Assert.assertThat;
import io.skysail.api.validation.DefaultValidationImpl;
import io.skysail.server.app.crm.ContactsGen;
import io.skysail.server.app.crm.domain.companies.CompaniesRepository;
import io.skysail.server.app.crm.domain.contacts.Contact;
import io.skysail.server.app.crm.domain.contacts.PostContactResource;
import io.skysail.server.testsupport.AbstractShiroTest;

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
import org.restlet.data.Form;

import de.twenty11.skysail.api.responses.ConstraintViolationsResponse;

@RunWith(MockitoJUnitRunner.class)
public class PostContactResourceTest extends AbstractShiroTest {

    @Rule
    public ExpectedException thrown = ExpectedException.none();

    @InjectMocks
    private PostContactResource resource;

    @Mock
    private ContactsGen app;

    @Before
    public void setUp() throws Exception {
        super.setUp();
        CompaniesRepository.getInstance().setDbService(dbService);
        Mockito.when(app.getValidatorService()).thenReturn(new DefaultValidationImpl());
        resource.init(null, request, response);
        form = Mockito.mock(Form.class);

    }

    @After
    public void tearDownSubject() {
        clearSubject();
    }

    @Test
    public void creates_registration_template() throws Exception {
        resource.init(null, null, null);
        Contact contact = resource.createEntityTemplate();
        assertThat(contact.getLastname(), is(nullValue()));
    }

    @Test
    public void missing_lastname_yields_failed_validation() {
        form.add("worksFor", null);
        Object result = (ConstraintViolationsResponse<?>) resource.post(form);

        assertValidationFailed(400, "Validation failed");
        assertOneConstraintViolation((ConstraintViolationsResponse<?>) result, "lastname", "may not be null");
    }

    protected void assertValidationFailed(int statusCode, String xStatusReason) {
        assertThat(response.getStatus().getCode(), is(statusCode));
        assertThat(response.getHeaders().getFirst("X-Status-Reason").getValue(), is(equalTo(xStatusReason)));
    }

}
