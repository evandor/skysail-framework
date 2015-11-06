package io.skysail.server.app.crm.test;

import static org.hamcrest.CoreMatchers.*;
import static org.junit.Assert.assertThat;
import io.skysail.server.app.crm.*;
import io.skysail.server.db.DbService;
import io.skysail.server.testsupport.AbstractShiroTest;

import java.util.Locale;

import org.junit.*;
import org.junit.rules.ExpectedException;
import org.mockito.Spy;
import org.restlet.data.Form;

public class CrmAppTest extends AbstractShiroTest {

    @Rule
    public ExpectedException thrown = ExpectedException.none();

    @Spy
    protected CrmApplication app;

    protected CrmRepository crmRepository;

    protected DbService dbService;

    public void setUp() {
        super.setUp();
        crmRepository = new CrmRepository();
        dbService = null;//new InMemoryDbService();
        crmRepository.setDbService(dbService);
        app.setCrmRepository(crmRepository);
       // Mockito.when(app.getValidatorService()).thenReturn(new DefaultValidationImpl());

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
