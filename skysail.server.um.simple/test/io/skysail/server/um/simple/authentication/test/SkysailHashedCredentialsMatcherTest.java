package io.skysail.server.um.simple.authentication.test;

import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;
import io.skysail.server.um.simple.authentication.SkysailAuthenticationInfo;
import io.skysail.server.um.simple.authentication.SkysailHashedCredentialsMatcher;

import org.apache.shiro.authc.AuthenticationInfo;
import org.apache.shiro.authc.AuthenticationToken;
import org.apache.shiro.authc.UsernamePasswordToken;
import org.apache.shiro.cache.MemoryConstrainedCacheManager;
import org.junit.Before;
import org.junit.Test;

import de.twenty11.skysail.server.um.domain.SkysailUser;

public class SkysailHashedCredentialsMatcherTest {

    private SkysailHashedCredentialsMatcher matcher;
    private AuthenticationInfo info;

    @Before
    public void setUp() throws Exception {
        matcher = new SkysailHashedCredentialsMatcher();
        SkysailUser user = new SkysailUser("username", "$2a$12$52R8v2QH3vQRz8NcdtOm5.HhE5tFPZ0T/.MpfUa9rBzOugK.btAHS",
                "#1");
        info = new SkysailAuthenticationInfo(user);
    }

    @Test
    public void wrong_credentials_do_not_match() {
        AuthenticationToken userProvidedToken = new UsernamePasswordToken("username", "wrong".toCharArray());
        boolean doCredentialsMatch = matcher.doCredentialsMatch(userProvidedToken, info);
        assertThat(doCredentialsMatch, is(false));
    }

    @Test
    public void proper_credentials_do_match() {
        AuthenticationToken userProvidedToken = new UsernamePasswordToken("username", "skysail".toCharArray());
        boolean doCredentialsMatch = matcher.doCredentialsMatch(userProvidedToken, info);
        assertThat(doCredentialsMatch, is(true));
    }

    @Test
    public void testName() {
        matcher.setCacheManager(new MemoryConstrainedCacheManager());
        AuthenticationToken userProvidedToken = new UsernamePasswordToken("username", "skysail".toCharArray());
        assertThat(matcher.getCacheSize(), is(0L));
        matcher.doCredentialsMatch(userProvidedToken, info);
        assertThat(matcher.getCacheSize(), is(1L));
        matcher.doCredentialsMatch(userProvidedToken, info);
        assertThat(matcher.getCacheSize(), is(1L));
    }
}
