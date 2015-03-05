package io.skysail.server.app.crm.domain.companies.test;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;
import io.skysail.api.validation.DefaultValidationImpl;
import io.skysail.server.app.crm.ContactsGen;
import io.skysail.server.app.crm.domain.CompanyWithId;
import io.skysail.server.app.crm.domain.companies.CompaniesRepository;
import io.skysail.server.app.crm.domain.companies.CompaniesResource;
import io.skysail.server.testsupport.AbstractShiroTest;

import java.util.List;

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
import org.restlet.data.MediaType;
import org.restlet.representation.Variant;

@RunWith(MockitoJUnitRunner.class)
public class CompaniesResourceTest extends AbstractShiroTest {

    @Rule
    public ExpectedException thrown = ExpectedException.none();

    @InjectMocks
    private CompaniesResource resource;

    @Mock
    private ContactsGen app;

    @Before
    public void setUp() throws Exception {
        super.setUp();
        Mockito.when(app.getValidatorService()).thenReturn(new DefaultValidationImpl());

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
        // Company company = resource.createEntityTemplate();
        // assertThat(company.getName(), is(nullValue()));
    }

    @Test
    public void html_request_retrieves_data_from_dbService() {
        dbService.persist(new CompanyWithId("admin", "7"));
        Variant variant = new Variant(MediaType.TEXT_HTML);
        List<String> entities = resource.getAsJson(variant);
        assertThat(entities.size(), is(1));

        assertThat(response.getStatus().getCode(), is(200));

        // assertValidationFailed(400, "Validation failed");
        // assertOneConstraintViolation((ConstraintViolationsResponse<?>)
        // result, "name", "may not be null");
    }

    protected void assertValidationFailed(int statusCode, String xStatusReason) {
        assertThat(response.getStatus().getCode(), is(statusCode));
        assertThat(response.getHeaders().getFirst("X-Status-Reason").getValue(), is(equalTo(xStatusReason)));
    }

}
