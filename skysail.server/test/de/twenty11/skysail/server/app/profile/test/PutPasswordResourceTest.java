package de.twenty11.skysail.server.app.profile.test;

import static org.hamcrest.Matchers.*;
import static org.junit.Assert.assertThat;
import io.skysail.api.validation.ValidatorService;

import javax.validation.Validator;

import org.junit.*;
import org.mockito.*;

import de.twenty11.skysail.server.app.SkysailRootApplication;
import de.twenty11.skysail.server.app.profile.PutPasswordResource;
import de.twenty11.skysail.server.validation.PutResourceTest;

@Ignore
public class PutPasswordResourceTest extends PutResourceTest {

    @Spy
    private PutPasswordResource resource;

    @Mock
    private SkysailRootApplication skysailRootApplication;

    @Override
    @Before
    public void setUp() throws Exception {
        super.setUp();

        Mockito.doReturn(skysailRootApplication).when(resource).getApplication();
        ValidatorService validatorService = Mockito.mock(ValidatorService.class);
        Validator validator = Mockito.mock(Validator.class);
        Mockito.doReturn(validator).when(validatorService).getValidator();
        Mockito.doReturn(validatorService).when(skysailRootApplication).getValidatorService();
        Mockito.doReturn(query).when(resource).getQuery();
        Mockito.when(skysailRootApplication.getUserManager()).thenReturn(userManager);

        resource.init(null, request, response);
    }

    @Test
    public void rejects_updating_password_if_old_password_is_null() throws Exception {
        Mockito.when(subjectUnderTest.getPrincipal()).thenReturn("admin");
        setSubject(subjectUnderTest);
        resource.put(form, null);
        assertThat(response.getStatus().getCode(), is(400));
    }

    @Test
    public void rejects_updating_password_if_new_password_is_null() throws Exception {
        Mockito.when(subjectUnderTest.getPrincipal()).thenReturn("admin");
        setSubject(subjectUnderTest);
        form.set("old", "oldPassword");
        resource.put(form, null);
        assertThat(response.getStatus().getCode(), is(400));
    }

    @Test
    public void rejects_updating_password_if_new_passwords_are_not_equal() throws Exception {

        Mockito.when(subjectUnderTest.getPrincipal()).thenReturn("admin");
        setSubject(subjectUnderTest);
        form.set("old", "oldPassword");
        form.set("password", "password");
        form.set("pwdRepeated", "pwdRepeated");

        resource.put(form, null);

        assertThat(response.getStatus().getCode(), is(400));
    }

    @Test
    public void rejects_updating_password_if_old_password_is_wrong() throws Exception {

        Mockito.when(subjectUnderTest.getPrincipal()).thenReturn("admin");
        setSubject(subjectUnderTest);

        form.set("old", "wrong");
        form.set("password", "password");
        form.set("pwdRepeated", "password");

        resource.put(form, null);

        assertThat(response.getStatus().getCode(), is(400));
    }

    @Test
    public void updates_password_if_everything_is_ok() {

        Mockito.when(subjectUnderTest.getPrincipal()).thenReturn("admin");
        setSubject(subjectUnderTest);

        form.set("old", "skysail");
        form.set("password", "password");
        form.set("pwdRepeated", "password");

        resource.put(form, null);

        Mockito.verify(userManager).update(adminUser);

        assertThat(response.getStatus().getCode(), is(303));
        assertThat(adminUser.getPassword(), is(not(equalTo(ADMIN_DEFAUTL_PASSWORD))));
    }

}
