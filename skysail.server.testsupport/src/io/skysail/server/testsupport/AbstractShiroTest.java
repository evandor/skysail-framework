package io.skysail.server.testsupport;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.Matchers.containsString;
import static org.junit.Assert.assertThat;
import io.skysail.api.validation.ValidatorService;

import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

import javax.validation.Validator;

import org.apache.shiro.SecurityUtils;
import org.apache.shiro.UnavailableSecurityManagerException;
import org.apache.shiro.mgt.SecurityManager;
import org.apache.shiro.subject.Subject;
import org.apache.shiro.subject.support.SubjectThreadState;
import org.apache.shiro.util.LifecycleUtils;
import org.apache.shiro.util.ThreadState;
import org.junit.AfterClass;
import org.mockito.Mockito;
import org.restlet.Request;
import org.restlet.Response;
import org.restlet.data.ClientInfo;
import org.restlet.data.Form;
import org.restlet.data.Reference;

import de.twenty11.skysail.api.responses.ConstraintViolationsResponse;

public class AbstractShiroTest {
    private static ThreadState subjectThreadState;

    protected ConcurrentMap<String, Object> attributes;
    protected Form form;
    protected Response response;
    protected Request request;

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

    protected static void setSecurityManager(SecurityManager securityManager) {
        SecurityUtils.setSecurityManager(securityManager);
    }

    protected static SecurityManager getSecurityManager() {
        return SecurityUtils.getSecurityManager();
    }

    public void setUp() throws Exception {
        attributes = new ConcurrentHashMap<String, Object>();
        setupSubject();
        setupValidation();
        setupRestlet();
    }

    private void setupRestlet() {
        request = Mockito.mock(Request.class);
        Mockito.when(request.getClientInfo()).thenReturn(new ClientInfo());
        Mockito.when(request.getAttributes()).thenReturn(attributes);
        Reference resourceRef = Mockito.mock(Reference.class);
        Mockito.when(request.getResourceRef()).thenReturn(resourceRef);
        response = new Response(request);
    }

    private void setupValidation() {
        ValidatorService validatorServiceMock = Mockito.mock(ValidatorService.class);
        Validator validator = Mockito.mock(Validator.class);
        Mockito.when(validatorServiceMock.getValidator()).thenReturn(validator);
    }

    private void setupSubject() {
        Subject subjectUnderTest = Mockito.mock(Subject.class);
        Mockito.when(subjectUnderTest.isAuthenticated()).thenReturn(true);
        Mockito.when(subjectUnderTest.getPrincipal()).thenReturn("admin");
        setSubject(subjectUnderTest);
    }

    @AfterClass
    public static void tearDownShiro() {
        doClearSubject();
        try {
            SecurityManager securityManager = getSecurityManager();
            LifecycleUtils.destroy(securityManager);
        } catch (UnavailableSecurityManagerException e) {
            // we don't care about this when cleaning up the test environment
            // (for example, maybe the subclass is a unit test and it didn't
            // need a SecurityManager instance because it was using only
            // mock Subject instances)
        }
        setSecurityManager(null);
    }

    protected void assertOneConstraintViolation(ConstraintViolationsResponse<?> violationsResponse, String path,
            String message) {
        assertThat(violationsResponse.getViolations().size(), is(1));
        assertThat(violationsResponse.getViolations().iterator().next().getPropertyPath(), containsString(path));
        assertThat(violationsResponse.getViolations().iterator().next().getMessage(), containsString(message));
    }

}
