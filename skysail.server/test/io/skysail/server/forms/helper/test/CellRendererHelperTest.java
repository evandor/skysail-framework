package io.skysail.server.forms.helper.test;

import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;

import java.util.Arrays;
import java.util.Date;
import java.util.HashSet;

import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;
import org.mockito.Mockito;

import io.skysail.api.links.Link;
import io.skysail.api.responses.ListServerResponse;
import io.skysail.api.responses.SkysailResponse;
import io.skysail.domain.html.Field;
import io.skysail.server.domain.jvm.ClassFieldModel;
import io.skysail.server.forms.ListView;
import io.skysail.server.forms.helper.CellRendererHelper;
import io.skysail.server.restlet.resources.SkysailServerResource;

public class CellRendererHelperTest {

    private ListServerResponse<String> listResponse;
    private SkysailServerResource<?> resource;
    
    @ListView(link = ResourceWithLink.class, truncate = 4)
    private static String linksToDefaultResourceField;
    
    private ClassFieldModel field;
    private SkysailResponse<?> response;
    
    @Field
    private String testfield;


    @Before
    public void setUp() throws Exception {
        listResponse = new ListServerResponse<>(null, Arrays.asList("1","2","3") );
        resource = Mockito.mock(SkysailServerResource.class);
        Mockito.when(resource.getLinks()).thenReturn(Arrays.asList(new Link.Builder("uri").definingClass(ResourceWithLink.class).refId("id").build()));
        response = new SkysailResponse<>();

    }

    @Test
    public void null_input_is_formatted_as_empty_String() {
        Object formatted = new CellRendererHelper(field, response).render(null, "id", null);
        assertThat(formatted, is(""));
    }

    @Test
    public void list_input_is_formatted_as_lists_size() {
        Object formatted = new CellRendererHelper(field, null).render(Arrays.asList("1","2","3"), "id", null);
        assertThat(formatted, is("#3"));
    }

    @Test
    public void set_input_is_formatted_as_sets_size() {
        @SuppressWarnings("serial")
        Object formatted = new CellRendererHelper(field, null).render(new HashSet<String>() {{ add("1"); add("2"); }}, "id", null);
        assertThat(formatted, is("2"));
    }

    @Test
    public void huge_long_is_interpreted_as_date_and_formatted() {
        Object formatted = new CellRendererHelper(field, null).render(1100000000L, "id", null);
        assertThat(((String)formatted), is("1970-01-13"));
    }

    @Test
    @Ignore // Not time-zone save
    public void other_non_string_objects_are_formatted_to_their_string_representation() {
        Object formatted = new CellRendererHelper(field, null).render(new Date(1100000000L), "id", null);
        assertThat(((String)formatted), is("Tue Jan 13 18:33:20 CET 1970"));
    }

    @Test
    public void strings_newlines() {
        Object formatted = new CellRendererHelper(field, null).render("a\rb\nc", "id", null);
        assertThat(((String)formatted), is("a&#13;b&#10;c"));
    }

    @Test
    public void simple_listResponse_is_formatted_as_columns_value() throws Exception {
        java.lang.reflect.Field f = getClass().getDeclaredField("testfield");
        field = new ClassFieldModel(f);
        Object formatted = new CellRendererHelper(field, listResponse).render("abc", "id", null);
        assertThat(((String)formatted), is("abc"));
    }

}
