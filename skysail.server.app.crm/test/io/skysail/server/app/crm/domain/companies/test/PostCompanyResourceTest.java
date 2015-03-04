package io.skysail.server.app.crm.domain.companies.test;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.CoreMatchers.nullValue;
import static org.junit.Assert.assertThat;
import io.skysail.server.app.crm.domain.companies.CompaniesRepository;
import io.skysail.server.app.crm.domain.companies.Company;
import io.skysail.server.app.crm.domain.companies.PostCompanyResource;
import io.skysail.server.app.crm.test.CrmAppTest;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.runners.MockitoJUnitRunner;

import de.twenty11.skysail.api.responses.ConstraintViolationsResponse;

@RunWith(MockitoJUnitRunner.class)
public class PostCompanyResourceTest extends CrmAppTest {

    @InjectMocks
    private PostCompanyResource resource;

    @Before
    public void setUp() throws Exception {
        super.setUp();
        CompaniesRepository.getInstance().setDbService(dbService);
        resource.init(null, request, response);
    }

    @Test
    public void creates_entity_template() throws Exception {
        resource.init(null, null, null);
        Company company = resource.createEntityTemplate();
        assertThat(company.getName(), is(nullValue()));
    }

    @Test
    public void missing_name_in_html_post_yields_failed_validation() {
        Object result = resource.post(form);

        assertValidationFailed(400, "Validation failed");
        assertOneConstraintViolation((ConstraintViolationsResponse<?>) result, "name", "may not be null");
    }

    @Test
    public void posting_valid_html_form_creates_new_entity() {
        form.add("name", "mycompany");
        Company post = (Company) resource.post(form);
        assertThat(post.getName(), is(equalTo("mycompany")));

    }
}
