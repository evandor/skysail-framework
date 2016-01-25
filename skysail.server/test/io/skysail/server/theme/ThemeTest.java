package io.skysail.server.theme;

import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;

import org.junit.*;
import org.mockito.Mockito;
import org.restlet.Request;
import org.restlet.data.Cookie;
import org.restlet.resource.Resource;
import org.restlet.util.Series;

import de.twenty11.skysail.server.Constants;
import io.skysail.server.theme.Theme;

public class ThemeTest {

    private Request request;
    private Series<Cookie> cookies;
    private Cookie cookie;
    private Resource resource;
    
    @Before
    @SuppressWarnings("unchecked")
    public void setUp() throws Exception {
        resource = Mockito.mock(Resource.class);
        request = Mockito.mock(Request.class);
        cookies = Mockito.mock(Series.class);
        Mockito.when(resource.getRequest()).thenReturn(request);
        Mockito.when(request.getCookies()).thenReturn(cookies);
    }

    @Test
    public void theme_is_derived_from_text_html_cookie() throws Exception {
        cookie = new Cookie(Constants.COOKIE_NAME_TEMPLATE, "text/html");
        Mockito.when(cookies.getFirst(Constants.COOKIE_NAME_TEMPLATE)).thenReturn(cookie);
        Theme theme = Theme.determineFrom(resource);
        assertThat(theme.getGuiFramework(),is(Theme.GuiFramework.TEXT));
    }
}
