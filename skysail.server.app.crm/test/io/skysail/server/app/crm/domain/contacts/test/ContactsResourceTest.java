package io.skysail.server.app.crm.domain.contacts.test;

import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;
import io.skysail.api.validation.DefaultValidationImpl;
import io.skysail.server.app.crm.domain.contacts.Contact;
import io.skysail.server.app.crm.domain.contacts.ContactsResource;
import io.skysail.server.app.crm.test.CrmAppTest;

import java.util.List;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mockito;
import org.mockito.runners.MockitoJUnitRunner;

@RunWith(MockitoJUnitRunner.class)
public class ContactsResourceTest extends CrmAppTest {

    @InjectMocks
    private ContactsResource resource;

    @Before
    public void setUp() throws Exception {
        super.setUp();

        // CrmRepository.getInstance().setDbService(dbService);
        Mockito.when(app.getValidatorService()).thenReturn(new DefaultValidationImpl());
        Mockito.when(app.getRepository()).thenReturn(crmRepository);
        resource.init(null, request, response);
    }

    @Test
    public void test() {
        dbService.persist(new Contact("admin"));
        List<Contact> entities = resource.getEntity();
        assertThat(entities.size(), is(1));
        assertThat(response.getStatus().getCode(), is(200));
    }

}
