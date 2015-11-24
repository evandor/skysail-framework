package io.skysail.server.forms.helper.test;

import static org.hamcrest.Matchers.is;
import static org.junit.Assert.assertThat;
import io.skysail.api.links.Link;
import io.skysail.api.responses.ListServerResponse;
import io.skysail.server.forms.*;
import io.skysail.server.forms.helper.CellRendererHelper;
import io.skysail.server.restlet.resources.SkysailServerResource;

import java.net.URL;
import java.util.*;

import org.junit.*;
import org.mockito.Mockito;

public class CellRendererHelperTest {

    private ListServerResponse<String> listResponse;
    private FormField formField;
    private SkysailServerResource<?> resource;

    @Before
    public void setUp() throws Exception {
        formField = Mockito.mock(FormField.class);
        listResponse = new ListServerResponse<>(Arrays.asList("1","2","3") );
        resource = Mockito.mock(SkysailServerResource.class);
        Mockito.when(resource.getLinks()).thenReturn(Arrays.asList(new Link.Builder("uri").definingClass(ResourceWithLink.class).refId("id").build()));
    }

    @Test
    public void null_input_is_formatted_as_empty_String() {
        FormField ff = null;
        Object formatted = new CellRendererHelper(ff,null).render(null, "id");
        assertThat(formatted, is(""));
    }

    @Test
    public void list_input_is_formatted_as_lists_size() {
        FormField ff = null;
        Object formatted = new CellRendererHelper(ff, null).render(Arrays.asList("1","2","3"), "id");
        assertThat(formatted, is("#3"));
    }

    @Test
    public void set_input_is_formatted_as_sets_size() {
        @SuppressWarnings({ "rawtypes", "unchecked", "serial" })
        FormField ff = null;
        Object formatted = new CellRendererHelper(ff, null).render(new HashSet() {{ add("1"); add("2"); }}, "id");
        assertThat(formatted, is("2"));
    }

    @Test
    public void huge_long_is_interpreted_as_date_and_formatted() {
        FormField ff = null;
        Object formatted = new CellRendererHelper(ff, null).render(1100000000L, "id");
        assertThat(((String)formatted), is("1970-01-13"));
    }

    @Test
    public void other_non_string_objects_are_formatted_to_their_string_representation() {
        FormField ff = null;
        Object formatted = new CellRendererHelper(ff, null).render(new Date(1100000000L), "id");
        assertThat(((String)formatted), is("Tue Jan 13 18:33:20 CET 1970"));
    }

    @Test
    public void strings_newlines() {
        FormField ff = null;
        Object formatted = new CellRendererHelper(ff, null).render("a\rb\nc", "id");
        assertThat(((String)formatted), is("a&#13;b&#10;c"));
    }

    @Test
    public void simple_listResponse_is_formatted_as_columns_value() {
        Object formatted = new CellRendererHelper(formField, listResponse).render("abc", "id");
        assertThat(((String)formatted), is("abc"));
    }

    private static URL theUrl;

    @Test
    public void formField_of_type_url_is_rendered_as_link() throws Exception {new CellRendererHelperTest().getClass().getDeclaredFields();
        FormField urlFormField = new FormField(CellRendererHelperTest.class.getDeclaredField("theUrl"), null);
        Object formatted = new CellRendererHelper(urlFormField, listResponse).render("abc", "id");
        assertThat(((String)formatted), is("<a href='abc' target=\"_blank\">abc</a>"));
    }

    @ListView(link = ResourceWithLink.class, truncate = 4)
    private static String linksToDefaultResourceField;

    @Test
    public void formField_with_linked_listView_is_rendered_as_truncated_link() throws Exception {
        FormField linksToDefaultResourceField = new FormField(CellRendererHelperTest.class.getDeclaredField("linksToDefaultResourceField"), resource);
        String formatted = new CellRendererHelper(linksToDefaultResourceField, listResponse).render("123456789", "id");
        assertThat(formatted, is("<a href='uri'><b><span title='123456789'>1...</span></b></a>"));
    }

}
