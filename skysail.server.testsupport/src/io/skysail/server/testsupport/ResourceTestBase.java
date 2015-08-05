package io.skysail.server.testsupport;

import static org.hamcrest.CoreMatchers.*;
import static org.hamcrest.Matchers.containsString;
import static org.junit.Assert.assertThat;
import io.skysail.api.responses.*;
import io.skysail.api.validation.*;
import io.skysail.server.app.SkysailApplication;
import io.skysail.server.restlet.resources.SkysailServerResource;

import java.math.BigInteger;
import java.security.SecureRandom;
import java.util.*;
import java.util.concurrent.*;
import java.util.concurrent.atomic.AtomicReference;

import lombok.Getter;

import org.apache.shiro.*;
import org.apache.shiro.subject.Subject;
import org.apache.shiro.subject.support.SubjectThreadState;
import org.apache.shiro.util.*;
import org.junit.*;
import org.junit.rules.ExpectedException;
import org.junit.runner.RunWith;
import org.mockito.Mockito;
import org.mockito.runners.MockitoJUnitRunner;
import org.restlet.*;
import org.restlet.data.*;
import org.restlet.engine.resource.VariantInfo;
import org.restlet.representation.Variant;
import org.restlet.resource.Resource;

import de.twenty11.skysail.server.services.*;
import de.twenty11.skysail.server.um.domain.SkysailUser;

@RunWith(MockitoJUnitRunner.class)
public class ResourceTestBase {

    protected static final String ADMIN_DEFAUTL_PASSWORD = "$2a$12$52R8v2QH3vQRz8NcdtOm5.HhE5tFPZ0T/.MpfUa9rBzOugK.btAHS";

    protected static final Variant HTML_VARIANT = new VariantInfo(MediaType.TEXT_HTML);
    protected static final Variant JSON_VARIANT = new VariantInfo(MediaType.APPLICATION_JSON);

    @Rule
    public ExpectedException thrown = ExpectedException.none();

    protected static ThreadState subjectThreadState;

    @Getter
    private ConcurrentMap<String, Object> attributes;

    protected Request request;
    protected Form form;
    protected ClientInfo clientInfo;
    protected Form query;
    protected AtomicReference<UserManager> userManagerRef = new AtomicReference<>();
    public Subject subjectUnderTest;
    protected SkysailUser adminUser;
    protected UserManager userManager;

    public SkysailApplication application;

    public static TestDb testDb;

    protected AtomicReference<ValidatorService> validatorServiceRef;
    protected AtomicReference<EncryptorService> encryptorServiceRef;

    protected Map<String, Response> responses = new HashMap<>();

    @BeforeClass
    public static void initDb() {
        testDb = new TestDb();
        testDb.startDb();
        testDb.activate();

        Locale englishLocale = Locale.ENGLISH;
        Locale.setDefault(englishLocale);
    }

    public void setUpFixture() {
        attributes = new ConcurrentHashMap<String, Object>();
        request = Mockito.mock(Request.class);
        Mockito.when(request.getAttributes()).thenReturn(attributes);
        Reference resourceRef = Mockito.mock(Reference.class);
        Reference targetRef = Mockito.mock(Reference.class);
        Reference baseRef = Mockito.mock(Reference.class);
        Mockito.when(resourceRef.getBaseRef()).thenReturn(baseRef);
        Mockito.when(baseRef.getTargetRef()).thenReturn(targetRef);
        Mockito.when(request.getResourceRef()).thenReturn(resourceRef);

        clientInfo = new ClientInfo();
        Mockito.when(request.getClientInfo()).thenReturn(clientInfo);

        userManager = Mockito.mock(UserManager.class);
        userManagerRef.set(userManager);
        adminUser = new SkysailUser("admin", ADMIN_DEFAUTL_PASSWORD, "#1");
        Mockito.when(userManager.findByUsername("admin")).thenReturn(adminUser);

        form = new Form();
        query = new Form();

        subjectUnderTest = Mockito.mock(Subject.class);
        Mockito.when(subjectUnderTest.isAuthenticated()).thenReturn(true);

    }

    public void setUpApplication(SkysailApplication application) {
        this.application = application;

        validatorServiceRef = new AtomicReference<>();
        encryptorServiceRef = new AtomicReference<>();

        ValidatorService validatorService = new DefaultValidationImpl();
        validatorServiceRef.set(validatorService);

        Mockito.doReturn(validatorServiceRef).when(application).getValidatorService();
        Mockito.doReturn(encryptorServiceRef).when(application).getEncryptorService();

    }

    public void setUpResource(Resource resource) throws Exception {
        Mockito.doReturn(application).when(resource).getApplication();
        Mockito.doReturn(query).when(resource).getQuery();

        Response response = new Response(request);
        responses.put(resource.getClass().getName(), response);
        resource.init(null, request, response);
    }

    @After
    public void tearDownSubject() {
        clearSubject();
    }

    @AfterClass
    public static void tearDownShiro() {
        doClearSubject();
        try {
            org.apache.shiro.mgt.SecurityManager securityManager = getSecurityManager();
            LifecycleUtils.destroy(securityManager);
        } catch (UnavailableSecurityManagerException e) {
            // we don't care about this when cleaning up the test environment
            // (for example, maybe the subclass is a unit test and it didn't
            // need a SecurityManager instance because it was using only
            // mock Subject instances)
        }
        setSecurityManager(null);
    }

    /**
     * Allows subclasses to set the currently executing {@link Subject}
     * instance.
     *
     * @param subject
     *            the Subject instance
     */
    public void setSubject(Subject subject) {
        clearSubject();
        subjectThreadState = createThreadState(subject);
        subjectThreadState.bind();
    }

    protected Subject getSubject() {
        return SecurityUtils.getSubject();
    }

    protected ThreadState createThreadState(Subject subject) {
        return new SubjectThreadState(subject);
    }

    /**
     * Clears Shiro's thread state, ensuring the thread remains clean for future
     * test execution.
     */
    protected void clearSubject() {
        doClearSubject();
    }

    private static void doClearSubject() {
        if (subjectThreadState != null) {
            subjectThreadState.clear();
            subjectThreadState = null;
        }
    }

    protected static void setSecurityManager(org.apache.shiro.mgt.SecurityManager securityManager) {
        SecurityUtils.setSecurityManager(securityManager);
    }

    protected static org.apache.shiro.mgt.SecurityManager getSecurityManager() {
        return SecurityUtils.getSecurityManager();
    }

    protected String randomString() {
        SecureRandom random = new SecureRandom();
        return new BigInteger(130, random).toString(32);
    }

    protected void assertValidationFailure(SkysailServerResource<?> resource, SkysailResponse<?> post, String path, String msg) {
        ConstraintViolationsResponse<?> skysailReponse = (ConstraintViolationsResponse<?>) post;
        assertThat(responses.get(resource.getClass().getName()).getStatus(), is(equalTo(Status.CLIENT_ERROR_BAD_REQUEST)));
        assertThat(responses.get(resource.getClass().getName()).getHeaders().getFirst("X-Status-Reason").getValue(), is(equalTo("Validation failed")));
        assertThat(skysailReponse.getViolations().size(), is(1));
        ConstraintViolationDetails violation = ((ConstraintViolationsResponse<?>) post).getViolations().iterator()
                .next();
        assertThat(violation.getPropertyPath(), containsString(path));
        assertThat(violation.getMessage(), is(containsString(msg)));
    }

}
