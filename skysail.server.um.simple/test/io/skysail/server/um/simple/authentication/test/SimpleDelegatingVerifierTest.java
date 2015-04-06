package io.skysail.server.um.simple.authentication.test;

import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;
import io.skysail.server.testsupport.AbstractShiroTest;
import io.skysail.server.um.simple.authentication.SimpleDelegatingVerifier;

import org.apache.shiro.authc.AuthenticationException;
import org.apache.shiro.authc.IncorrectCredentialsException;
import org.apache.shiro.authc.LockedAccountException;
import org.apache.shiro.authc.UnknownAccountException;
import org.apache.shiro.subject.Subject;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mockito;

public class SimpleDelegatingVerifierTest extends AbstractShiroTest {

    private SimpleDelegatingVerifier verifier;

    @Before
    public void setUp() {
        verifier = new SimpleDelegatingVerifier();
        subjectUnderTest = Mockito.mock(Subject.class);
        setSubject(subjectUnderTest);
    }

    @After
    public void tearDownSubject() {
        // 3. Unbind the subject from the current thread:
        clearSubject();
    }

    @Test
    public void authenticated_yields_valid_result() {
        Mockito.when(subjectUnderTest.isAuthenticated()).thenReturn(true);
        int verified = verifier.verify("identifier", "secret".toCharArray());
        assertThat(verified, is(org.restlet.security.Verifier.RESULT_VALID));
    }

    @Test
    public void unknownAccount_yields_unvalid_result() {
        Mockito.doThrow(UnknownAccountException.class).when(subjectUnderTest).login(Mockito.any());
        assertThat(verifier.verify("identifier", "secret".toCharArray()),
                is(org.restlet.security.Verifier.RESULT_INVALID));
    }

    @Test
    public void incorrectCredentials_yields_unvalid_result() {
        Mockito.doThrow(IncorrectCredentialsException.class).when(subjectUnderTest).login(Mockito.any());
        assertThat(verifier.verify("identifier", "secret".toCharArray()),
                is(org.restlet.security.Verifier.RESULT_INVALID));
    }

    @Test
    public void lockedAccount_yields_unvalid_result() {
        Mockito.doThrow(LockedAccountException.class).when(subjectUnderTest).login(Mockito.any());
        assertThat(verifier.verify("identifier", "secret".toCharArray()),
                is(org.restlet.security.Verifier.RESULT_INVALID));
    }

    @Test
    public void authenticationException_yields_unvalid_result() {
        Mockito.doThrow(AuthenticationException.class).when(subjectUnderTest).login(Mockito.any());
        assertThat(verifier.verify("identifier", "secret".toCharArray()),
                is(org.restlet.security.Verifier.RESULT_INVALID));
    }

}
