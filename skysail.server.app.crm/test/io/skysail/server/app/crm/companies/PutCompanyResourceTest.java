package io.skysail.server.app.crm.companies;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.CoreMatchers.nullValue;
import static org.junit.Assert.assertThat;
import io.skysail.server.app.crm.companies.Company;
import io.skysail.server.app.crm.companies.resources.PutCompanyResource;
import io.skysail.server.app.crm.test.CrmAppTest;

import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.runners.MockitoJUnitRunner;

@RunWith(MockitoJUnitRunner.class)
public class PutCompanyResourceTest extends CrmAppTest {

    @InjectMocks
    private PutCompanyResource resource;

    @Before
    public void setUp() throws Exception {
        super.setUp();
    }

    @Test
    @Ignore
    public void test() {
        Company myCompany = new Company("admin");
        myCompany.setName("thename");
        String id = (String) dbService.persist(myCompany);

        attributes.put("id", id);
        resource.init(null, request, response);

        form.add("name", "the new name");
        resource.put(form);

        assertThat(response.getStatus().getCode(), is(200));
        assertThat(response.getHeaders().getFirst("X-Status-Reason"), is(nullValue()));

        Company updatedCompany = dbService.findObjectById(Company.class, id);
        assertThat(updatedCompany.getName(), is(equalTo("the new name")));
        assertThat(updatedCompany.getCreator(), is(equalTo("admin")));
    }

}
