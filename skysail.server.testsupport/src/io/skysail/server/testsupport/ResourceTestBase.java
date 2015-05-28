package io.skysail.server.testsupport;

import io.skysail.api.validation.DefaultValidationImpl;
import io.skysail.api.validation.ValidatorService;
import io.skysail.server.app.SkysailApplication;

import java.math.BigInteger;
import java.security.SecureRandom;
import java.util.Locale;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;
import java.util.concurrent.atomic.AtomicReference;

import org.apache.shiro.SecurityUtils;
import org.apache.shiro.UnavailableSecurityManagerException;
import org.apache.shiro.subject.Subject;
import org.apache.shiro.subject.support.SubjectThreadState;
import org.apache.shiro.util.LifecycleUtils;
import org.apache.shiro.util.ThreadState;
import org.junit.After;
import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Rule;
import org.junit.rules.ExpectedException;
import org.junit.runner.RunWith;
import org.mockito.Mockito;
import org.mockito.runners.MockitoJUnitRunner;
import org.restlet.Request;
import org.restlet.Response;
import org.restlet.data.ClientInfo;
import org.restlet.data.Form;
import org.restlet.data.Reference;
import org.restlet.resource.Resource;

import de.twenty11.skysail.server.services.EncryptorService;
import de.twenty11.skysail.server.services.UserManager;
import de.twenty11.skysail.server.um.domain.SkysailUser;

@RunWith(MockitoJUnitRunner.class)
public class ResourceTestBase {

    protected static final String ADMIN_DEFAUTL_PASSWORD = "$2a$12$52R8v2QH3vQRz8NcdtOm5.HhE5tFPZ0T/.MpfUa9rBzOugK.btAHS";

    @Rule
    public ExpectedException thrown = ExpectedException.none();

    protected static ThreadState subjectThreadState;

    protected ConcurrentMap<String, Object> attributes;
    protected Request request;
    protected Response response;
    protected Form form;
    protected ClientInfo clientInfo;
    protected Form query;
    protected AtomicReference<UserManager> userManagerRef = new AtomicReference<>();
    protected Subject subjectUnderTest;
    protected SkysailUser adminUser;
    protected UserManager userManager;

    protected SkysailApplication application;

    protected static TestDb testDb;

    protected AtomicReference<ValidatorService> validatorServiceRef;
    protected AtomicReference<EncryptorService> encryptorServiceRef;

    @BeforeClass
    public static void initDb() {
        testDb = new TestDb();
        testDb.startDb();
        testDb.activate();
        
        Locale englishLocale = Locale.ENGLISH;
        Locale.setDefault(englishLocale);
    }

    public void setUp(SkysailApplication application, Resource resource) throws Exception {
        attributes = new ConcurrentHashMap<String, Object>();
        request = Mockito.mock(Request.class);
        Mockito.when(request.getAttributes()).thenReturn(attributes);

        response = new Response(request);

        Reference resourceRef = Mockito.mock(Reference.class);

        Reference targetRef = Mockito.mock(Reference.class);
        Reference baseRef = Mockito.mock(Reference.class); // http://localhost:2016/root/_profile/password/
        Mockito.when(resourceRef.getBaseRef()).thenReturn(baseRef);
        Mockito.when(baseRef.getTargetRef()).thenReturn(targetRef);

        Mockito.when(request.getResourceRef()).thenReturn(resourceRef);

        clientInfo = new ClientInfo();
        Mockito.when(request.getClientInfo()).thenReturn(clientInfo);

        form = new Form();

        userManager = Mockito.mock(UserManager.class);
        userManagerRef.set(userManager);
        adminUser = new SkysailUser("admin", ADMIN_DEFAUTL_PASSWORD, "#1");
        Mockito.when(userManager.findByUsername("admin")).thenReturn(adminUser);

        query = new Form();

        subjectUnderTest = Mockito.mock(Subject.class);
        Mockito.when(subjectUnderTest.isAuthenticated()).thenReturn(true);

        validatorServiceRef = new AtomicReference<>();
        encryptorServiceRef = new AtomicReference<>();

        ValidatorService validatorService = new DefaultValidationImpl();
        validatorServiceRef.set(validatorService);

        this.application = application;

        Mockito.doReturn(validatorServiceRef).when(application).getValidatorService();
        Mockito.doReturn(encryptorServiceRef).when(application).getEncryptorService();

        Mockito.doReturn(application).when(resource).getApplication();
        Mockito.doReturn(query).when(resource).getQuery();

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
        // testDb.
    }

    /**
     * Allows subclasses to set the currently executing {@link Subject}
     * instance.
     *
     * @param subject
     *            the Subject instance
     */
    protected void setSubject(Subject subject) {
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

}
