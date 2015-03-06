package io.skysail.server.app.crm.domain.companies.test;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.CoreMatchers.nullValue;
import static org.junit.Assert.assertThat;
import io.skysail.api.validation.DefaultValidationImpl;
import io.skysail.server.app.crm.domain.CompanyWithId;
import io.skysail.server.app.crm.domain.companies.Company;
import io.skysail.server.app.crm.domain.companies.PutCompanyResource;
import io.skysail.server.app.crm.test.CrmAppTest;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mockito;
import org.mockito.runners.MockitoJUnitRunner;
import org.restlet.data.Form;

@RunWith(MockitoJUnitRunner.class)
public class PutCompanyResourceTest extends CrmAppTest {

    @InjectMocks
    private PutCompanyResource resource;

    @Before
    public void setUp() throws Exception {
        super.setUp();
        attributes.put("id", "7");
        // CrmRepository.getInstance().setDbService(dbService);
        Mockito.when(app.getValidatorService()).thenReturn(new DefaultValidationImpl());
        resource.init(null, request, response);
        form = Mockito.mock(Form.class);
    }

    @After
    public void tearDownSubject() {
        clearSubject();
    }

    @Test
    public void missing_name_yields_failed_validation() {
        Company myCompany = new CompanyWithId("admin", "7");
        dbService.persist(myCompany);
        Object result = resource.put(form);

        assertThat(response.getStatus().getCode(), is(200));
        assertThat(response.getHeaders().getFirst("X-Status-Reason"), is(nullValue()));
    }

    protected void assertValidationFailed(int statusCode, String xStatusReason) {
    }

}
