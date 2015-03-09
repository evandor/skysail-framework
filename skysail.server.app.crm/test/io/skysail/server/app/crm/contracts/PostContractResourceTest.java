package io.skysail.server.app.crm.contracts;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.CoreMatchers.nullValue;
import static org.junit.Assert.assertThat;
import io.skysail.server.app.crm.contracts.Contract;
import io.skysail.server.app.crm.contracts.PostContractResource;
import io.skysail.server.app.crm.test.CrmAppTest;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.runners.MockitoJUnitRunner;

@RunWith(MockitoJUnitRunner.class)
public class PostContractResourceTest extends CrmAppTest {

    @InjectMocks
    private PostContractResource resource;

    @Before
    public void setUp() throws Exception {
        super.setUp();
        // CrmRepository.getInstance().setDbService(dbService);
        resource.init(null, request, response);
    }

    @Test
    public void creates_entity_template() throws Exception {
        resource.init(null, null, null);
        Contract contract = resource.createEntityTemplate();
        assertThat(contract.getString("name"), is(nullValue()));
    }

    @Test
    public void test() {
        form.add("name", "myname");
        Object result = resource.post(form);

        // assertValidationFailed(400, "Validation failed");
        // assertOneConstraintViolation((ConstraintViolationsResponse<?>)
        // result, "lastname", "may not be null");
    }

}
