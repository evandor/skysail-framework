package io.skysail.server.app.crm.test;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;
import io.skysail.api.validation.DefaultValidationImpl;
import io.skysail.server.app.crm.ContactsGen;
import io.skysail.server.testsupport.AbstractShiroTest;

import java.util.Locale;

import org.junit.After;
import org.junit.Rule;
import org.junit.rules.ExpectedException;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.restlet.data.Form;

public class CrmAppTest extends AbstractShiroTest {

    @Rule
    public ExpectedException thrown = ExpectedException.none();

    @Mock
    protected ContactsGen app;

    public void setUp() throws Exception {
        super.setUp();
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
