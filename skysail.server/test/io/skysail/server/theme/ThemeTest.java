package io.skysail.server.theme;

import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;

import org.junit.*;
import org.mockito.Mockito;
import org.restlet.*;
import org.restlet.data.*;
import org.restlet.engine.resource.VariantInfo;
import org.restlet.representation.Variant;
import org.restlet.resource.Resource;
import org.restlet.util.Series;

import de.twenty11.skysail.server.Constants;

public class ThemeTest {

    private Request request;
    private Series<Cookie> cookies;
    private Cookie cookie;
    private Resource resource;
    private Response response;
    private Variant target;
    
    @Before
    @SuppressWarnings("unchecked")
    public void setUp() throws Exception {
        resource = Mockito.mock(Resource.class);
        request = Mockito.mock(Request.class);
        cookies = Mockito.mock(Series.class);
        Mockito.when(resource.getRequest()).thenReturn(request);
        response = Mockito.mock(Response.class);
        Mockito.when(resource.getResponse()).thenReturn(response);
        Mockito.when(request.getCookies()).thenReturn(cookies);
        target = new VariantInfo(MediaType.TEXT_HTML);
    }
    
    @Test
    public void no_theme_from_request_nor_cookie_nor_mediatype_yields_bootstrap_theme() throws Exception {
        //cookie = new Cookie(Constants.COOKIE_NAME_TEMPLATE, "text/html");
        Mockito.when(cookies.getFirst(Constants.COOKIE_NAME_TEMPLATE)).thenReturn(null);
        Theme theme = Theme.determineFrom(resource, target);
        assertThat(theme.getVariant(),is(Theme.Variant.BOOTSTRAP));
    }


    @Test
    public void theme_is_derived_from_text_html_cookie() throws Exception {
        cookie = new Cookie(Constants.COOKIE_NAME_TEMPLATE, "text/html");
        Mockito.when(cookies.getFirst(Constants.COOKIE_NAME_TEMPLATE)).thenReturn(cookie);
        Theme theme = Theme.determineFrom(resource, target);
       // assertThat(theme.getGuiFramework(),is(Theme.GuiFramework.TEXT));
    }
    

    @Test
    public void theme_is_derived_from_query_if_existent() throws Exception {
        Form form = new Form();
        form.add("_theme", "text/uikit");
        Mockito.when(resource.getQuery()).thenReturn(form);
        Reference reference = Mockito.mock(Reference.class);
        Mockito.when(request.getResourceRef()).thenReturn(reference);
        Mockito.when(reference.getPath()).thenReturn("path");
        @SuppressWarnings("unchecked")
        Series<CookieSetting> cookieSettings = new Series(CookieSetting.class);
        Mockito.when(response.getCookieSettings()).thenReturn(cookieSettings);
        cookie = new Cookie(Constants.COOKIE_NAME_TEMPLATE, "text/uikit");
        Mockito.when(cookies.getFirst(Constants.COOKIE_NAME_TEMPLATE)).thenReturn(cookie);

        Theme theme = Theme.determineFrom(resource, target);
        
       // assertThat(theme.getGuiFramework(),is(Theme.GuiFramework.TEXT));
        assertThat(theme.getVariant(),is(Theme.Variant.UIKIT));
    }
}
