package de.twenty11.skysail.api.responses.test;

import static org.hamcrest.Matchers.contains;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.is;
import static org.junit.Assert.assertThat;

import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Ignore;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;
import org.mockito.Mockito;
import org.restlet.Request;
import org.restlet.data.Method;
import org.restlet.data.Reference;

import de.twenty11.skysail.api.responses.LinkHeaderRelation;
import de.twenty11.skysail.api.responses.Linkheader;

public class LinkheaderTest {

    @Rule
    public ExpectedException thrown = ExpectedException.none();

    private Reference resourceRef;
    private Request request;

    @BeforeClass
    public static void setUpBeforeClass() throws Exception {
    }

    @Before
    public void setUp() throws Exception {
        request = Mockito.mock(Request.class);
        resourceRef = Mockito.mock(Reference.class);
        Mockito.when(request.getOriginalRef()).thenReturn(resourceRef);
    }
    
    @Test
    public void builder_creates_link_with_proper_uri() throws Exception {
        Linkheader link = new Linkheader.Builder("uri").build();
        assertThat(link.getUri(), is(equalTo("uri")));
    }

    @Test
    public void builder_rejects_link_with_null_uri() throws Exception {
        thrown.expect(NullPointerException.class);
        new Linkheader.Builder(null).build();
    }

    @Test
    public void builder_creates_link_with_default_linkrelation() throws Exception {
        Linkheader link = new Linkheader.Builder("uri").build();
        assertThat(link.getRel(), is(LinkHeaderRelation.ITEM));
    }
    
    @Test
    public void builder_creates_link_with_provided_title() throws Exception {
        Linkheader link = new Linkheader.Builder("uri").title("title").build();
        assertThat(link.getTitle(), is(equalTo("title")));
    }

    @Test
    public void builder_creates_link_which_needs_authentication_by_default() throws Exception {
        Linkheader link = new Linkheader.Builder("uri").title("title").build();
        assertThat(link.getNeedsAuthentication(), is(true));
    }

    @Test
    public void builder_creates_link_which_doesnot_need_authentication_if_switched_off() throws Exception {
        Linkheader link = new Linkheader.Builder("uri").title("title").authenticationNeeded(false).build();
        assertThat(link.getNeedsAuthentication(), is(false));
    }

    @Test
    public void creates_a_linkheader_segment_for_a_create_form_relation() {
        Linkheader linkheader = new Linkheader.Builder("uri").relation(LinkHeaderRelation.CREATE_FORM).build();
        assertThat(linkheader.toString(null, "path"), is(equalTo("<pathuri>; rel=\"create-form\"; title=\"create-form\"; verbs=\"GET\"")));
    }
    
    @Test
    public void a_linkheaders_relation_can_be_changed_after_build() {
        Linkheader linkheader = new Linkheader.Builder("uri").relation(LinkHeaderRelation.CREATE_FORM).build();
        linkheader.setRelation(LinkHeaderRelation.SELF);
        assertThat(linkheader.toString(null, "path"), is(equalTo("<pathuri>; rel=\"self\"; title=\"create-form\"; verbs=\"GET\"")));
    }

    @Test
    public void parses_a_serialized_linkheader_to_an_linkheader_object() {
        String linkheader = "<pathuri>; rel=\"create-form\"; title=\"create-form\"; verbs=\"GET\"";
        assertThat(Linkheader.valueOf(linkheader).getUri(), is(equalTo("pathuri")));
        assertThat(Linkheader.valueOf(linkheader).getRel(), is(equalTo(LinkHeaderRelation.CREATE_FORM)));
        
    }

    @Test
    public void parses_a_serialized_linkheader_to_an_linkheader_object2() {
        String linkheader = "<pathuri>; rel=\"about\"; title=\"about me\"; verbs=\"POST\"";
        assertThat(Linkheader.valueOf(linkheader).getUri(), is(equalTo("pathuri")));
        assertThat(Linkheader.valueOf(linkheader).getRel(), is(equalTo(LinkHeaderRelation.ABOUT)));
        assertThat(Linkheader.valueOf(linkheader).getTitle(), is(equalTo("about me")));
        assertThat(Linkheader.valueOf(linkheader).getVerbs(), contains(Method.POST));
    }
    
    @Test
    @Ignore
    public void matches_id_from_request_with_trailing_slash() throws Exception {
        Linkheader linkheader = new Linkheader.Builder("/wiki/spaces/{spaceId}/pages/").relation(LinkHeaderRelation.CREATE_FORM).build();
        Mockito.when(resourceRef.getPath(true)).thenReturn("/wiki/spaces/15:0");

        String link = linkheader.toString(request, "");
        
        assertThat(link, is(equalTo("</wiki/spaces/15:0/pages/>; rel=\"create-form\"; title=\"create-form\"; verbs=\"GET\"")));
    }

    @Test
    @Ignore
    public void matches_id_from_request_without_trailing_slash() throws Exception {
        Linkheader linkheader = new Linkheader.Builder("/wiki/spaces/{spaceId}/pages").relation(LinkHeaderRelation.CREATE_FORM).build();
        Mockito.when(resourceRef.getPath(true)).thenReturn("/wiki/spaces/15:0");

        String link = linkheader.toString(request, "");
        
        assertThat(link, is(equalTo("</wiki/spaces/15:0/pages>; rel=\"create-form\"; title=\"create-form\"; verbs=\"GET\"")));
    }

}
