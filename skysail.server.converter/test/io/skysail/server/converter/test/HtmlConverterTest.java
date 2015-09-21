package io.skysail.server.converter.test;

import static org.hamcrest.CoreMatchers.*;
import static org.junit.Assert.assertThat;
import io.skysail.server.converter.HtmlConverter;
import io.skysail.server.restlet.resources.EntityServerResource;

import java.util.*;

import org.junit.*;
import org.junit.rules.ExpectedException;
import org.mockito.Mockito;
import org.restlet.data.MediaType;
import org.restlet.engine.resource.VariantInfo;
import org.restlet.representation.*;
import org.restlet.resource.Resource;

import de.twenty11.skysail.server.services.*;

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
        assertThat(variants.size(), is(3));
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

}
