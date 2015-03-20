package io.skysail.server.app.crm.contacts;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.CoreMatchers.nullValue;
import static org.junit.Assert.assertThat;
import io.skysail.api.responses.ConstraintViolationsResponse;
import io.skysail.server.app.crm.test.CrmAppTest;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.runners.MockitoJUnitRunner;

@RunWith(MockitoJUnitRunner.class)
public class PostContactResourceTest extends CrmAppTest {

    @InjectMocks
    private PostContactResource resource;

    @Before
    public void setUp() throws Exception {
        super.setUp();
        // CrmRepository.getInstance().setDbService(dbService);
        resource.init(null, request, response);
    }

    @Test
    public void creates_entity_template() throws Exception {
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

}
