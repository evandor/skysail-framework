package io.skysail.server.converter.test;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;
import io.skysail.server.converter.HtmlConverter;

import java.util.Arrays;
import java.util.List;

import org.junit.Before;
import org.junit.Ignore;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;
import org.mockito.Mockito;
import org.osgi.framework.Bundle;
import org.osgi.framework.BundleContext;
import org.restlet.Request;
import org.restlet.data.Cookie;
import org.restlet.data.MediaType;
import org.restlet.engine.resource.VariantInfo;
import org.restlet.engine.util.CookieSeries;
import org.restlet.representation.Representation;
import org.restlet.representation.Variant;
import org.restlet.resource.Resource;
import org.restlet.util.Series;

import de.twenty11.skysail.server.app.SkysailApplication;
import de.twenty11.skysail.server.core.restlet.EntityServerResource;
import de.twenty11.skysail.server.services.MenuItem;
import de.twenty11.skysail.server.services.MenuItemProvider;
import de.twenty11.skysail.server.services.UserManager;

public class HtmlConverterTest {

    @Rule
    public ExpectedException thrown = ExpectedException.none();

    private HtmlConverter htmlConverter;

    private MenuItemProvider menuItemProvider = new MenuItemProvider() {
        @Override
        public List<MenuItem> getMenuEntries() {
            return Arrays.asList(new MenuItem("name", "link"));
        }
    };

    @Before
    public void setUp() {
        htmlConverter = new HtmlConverter();
    }

    @Test
    public void returns_added_menuItemProvider() throws Exception {
        htmlConverter.addMenuProvider(menuItemProvider);
        List<MenuItem> menuEntries = htmlConverter.getMenuProviders().iterator().next().getMenuEntries();
        assertThat(htmlConverter.getMenuProviders().size(), is(1));
        assertThat(menuEntries.size(), is(1));
        assertThat(menuEntries.get(0).getLink(), is(equalTo("link")));
    }

    @Test
    public void removed_menuItemProvider_is_not_available_anymore() throws Exception {
        htmlConverter.addMenuProvider(menuItemProvider);
        htmlConverter.removeMenuProvider(menuItemProvider);
        assertThat(htmlConverter.getMenuProviders().size(), is(0));
    }

    @Test
    public void returns_added_usermanager() throws Exception {
        UserManager userManager = Mockito.mock(UserManager.class);
        htmlConverter.setUserManager(userManager);
        assertThat(htmlConverter.getUserManager(), is(equalTo(userManager)));
    }

    @Test
    public void getObjectClasses_throws_exception() throws Exception {
        Variant source = Mockito.mock(Variant.class);
        thrown.expect(RuntimeException.class);
        htmlConverter.getObjectClasses(source);
    }

    @Test
    public void toObjectClasses_throws_exception() throws Exception {
        thrown.expect(RuntimeException.class);
        htmlConverter.toObject(Mockito.mock(Representation.class), String.class, Mockito.mock(Resource.class));
    }

    @Test
    public void returns_variants() throws Exception {
        Class<?> source = String.class;
        List<VariantInfo> variants = htmlConverter.getVariants(source);
        assertThat(variants.size(), is(1));
        assertThat(variants.get(0).getMediaType().getName(), is(equalTo("treeform/*")));
    }

    @Test
    public void score_for_json_target_yields_default_score_of_0dot5() throws Exception {
        Variant target = new VariantInfo(MediaType.APPLICATION_JSON);
        Resource resource = Mockito.mock(EntityServerResource.class);
        float score = htmlConverter.score("source", target, resource);
        assertThat(score, is(0.5F));
    }

    @Test
    public void score_for_html_target_yields_0dot95() throws Exception {
        Variant target = new VariantInfo(MediaType.TEXT_HTML);
        Resource resource = Mockito.mock(EntityServerResource.class);
        float score = htmlConverter.score("source", target, resource);
        assertThat(score, is(0.95F));
    }

    @Test
    @Ignore
    public void second_score_method_returs_minus1() throws Exception {
        float score = htmlConverter.score(Mockito.mock(Representation.class), new VariantInfo(MediaType.TEXT_HTML),
                Mockito.mock(Resource.class));
        assertThat(score, is(-1.0F));
    }

    @Test
    @Ignore
    public void testName() throws Exception {
        EntityServerResource<?> resource = Mockito.mock(EntityServerResource.class);
        Request request = Mockito.mock(Request.class);
        Series<Cookie> cookies = new CookieSeries();
        Mockito.when(request.getCookies()).thenReturn(cookies);
        Mockito.when(resource.getRequest()).thenReturn(request);
        SkysailApplication application = Mockito.mock(SkysailApplication.class);
        Bundle bundle = Mockito.mock(Bundle.class);
        Mockito.when(bundle.getSymbolicName()).thenReturn("skysail.server.ext.converter.st");
        BundleContext bundleContext = Mockito.mock(BundleContext.class);
        Bundle[] bundles = new Bundle[] { bundle };
        Mockito.when(bundleContext.getBundles()).thenReturn(bundles);
        Mockito.when(bundle.getBundleContext()).thenReturn(bundleContext);
        Mockito.when(application.getBundle()).thenReturn(bundle);
        Mockito.when(resource.getApplication()).thenReturn(application);
        Variant target = new VariantInfo(MediaType.TEXT_HTML);
        Representation representation = htmlConverter.toRepresentation("originalSource", target, resource);
        System.out.println(representation);
    }

}
