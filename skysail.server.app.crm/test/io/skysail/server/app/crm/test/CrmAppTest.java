package io.skysail.server.app.crm.test;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;
import io.skysail.api.validation.DefaultValidationImpl;
import io.skysail.server.app.crm.CrmApplication;
import io.skysail.server.app.crm.CrmRepository;
import io.skysail.server.testsupport.AbstractShiroTest;
import io.skysail.server.testsupport.InMemoryDbService;

import java.util.Locale;

import org.junit.After;
import org.junit.Rule;
import org.junit.rules.ExpectedException;
import org.mockito.Mockito;
import org.mockito.Spy;
import org.restlet.data.Form;

import de.twenty11.skysail.server.core.db.DbService2;

public class CrmAppTest extends AbstractShiroTest {

    @Rule
    public ExpectedException thrown = ExpectedException.none();

    @Spy
    protected CrmApplication app;

    protected CrmRepository crmRepository;

    protected DbService2 dbService;

    public void setUp() throws Exception {
        super.setUp();
        crmRepository = new CrmRepository();
        dbService = new InMemoryDbService();
        crmRepository.setDbService(dbService);
        app.setCrmRepository(crmRepository);
        Mockito.when(app.getValidatorService()).thenReturn(new DefaultValidationImpl());

        form = new Form();
        Locale locale_en = new Locale("en");
        Locale.setDefault(locale_en);
    }

    @After
    public void tearDownSubject() {
        clearSubject();
    }

    protected void assertValidationFailed(int statusCode, String xStatusReason) {
        assertThat(response.getStatus().getCode(), is(statusCode));
        assertThat(response.getHeaders().getFirst("X-Status-Reason").getValue(), is(equalTo(xStatusReason)));
    }

}
