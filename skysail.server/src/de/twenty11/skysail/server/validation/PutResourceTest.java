package de.twenty11.skysail.server.validation;

import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

import org.apache.shiro.SecurityUtils;
import org.apache.shiro.UnavailableSecurityManagerException;
import org.apache.shiro.subject.Subject;
import org.apache.shiro.subject.support.SubjectThreadState;
import org.apache.shiro.util.LifecycleUtils;
import org.apache.shiro.util.ThreadState;
import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
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

import de.twenty11.skysail.server.services.UserManager;
import de.twenty11.skysail.server.um.domain.SkysailUser;

@RunWith(MockitoJUnitRunner.class)
public class PutResourceTest {

    protected static final String ADMIN_DEFAUTL_PASSWORD = "$2a$12$52R8v2QH3vQRz8NcdtOm5.HhE5tFPZ0T/.MpfUa9rBzOugK.btAHS";

    @Rule
    public ExpectedException thrown = ExpectedException.none();

    private static ThreadState subjectThreadState;

    protected ConcurrentMap<String, Object> attributes;

    protected Request request;

    protected Response response;

    protected Form form;

    protected ClientInfo clientInfo;

    protected Form query;

    protected UserManager userManager;

    protected Subject subjectUnderTest;

    protected SkysailUser adminUser;

    @Before
    public void setUp() throws Exception {
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
        adminUser = new SkysailUser("admin", ADMIN_DEFAUTL_PASSWORD, "#1");
        Mockito.when(userManager.findByUsername("admin")).thenReturn(adminUser);

        query = new Form();

        subjectUnderTest = Mockito.mock(Subject.class);
        Mockito.when(subjectUnderTest.isAuthenticated()).thenReturn(true);

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

}
