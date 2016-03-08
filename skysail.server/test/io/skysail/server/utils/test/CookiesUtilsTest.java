package io.skysail.server.utils.test;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.CoreMatchers.nullValue;
import static org.junit.Assert.assertThat;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;
import org.restlet.Request;

import io.skysail.server.utils.CookiesUtils;

@RunWith(MockitoJUnitRunner.class)
public class CookiesUtilsTest {

    @Mock
    private Request request;

    @Before
    public void setUp()  {
    }

    @Test
    public void createCookie()  {

    }

    @Test
    public void getThemeFromCookie_is_null_if_request_does_not_have_any_cookies()  {
        String themeFromCookie = CookiesUtils.getThemeFromCookie(request);
        assertThat(themeFromCookie,is(nullValue()));
    }

//    @Test
//    public void getThemeFromCookie_is_null_if_request_does_not_have_theme_cookies()  {
//        when(request.getCookies()).thenReturn(cookies);
//        String themeFromCookie = CookiesUtils.getThemeFromCookie(request);
//        assertThat(themeFromCookie,is(nullValue()));
//    }

    @Test
    public void getModeFromCookie()  {

    }

    @Test
    public void getMainPageFromCookie()  {

    }

    @Test
    public void getInstallationFromCookie()  {

    }

    @Test
    public void getEntriesPerPageFromCookie()  {

    }

    @Test
    public void getReferrerFromCookie()  {

    }

}
