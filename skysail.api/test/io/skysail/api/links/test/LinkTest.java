package io.skysail.api.links.test;

import static org.hamcrest.CoreMatchers.nullValue;
import static org.hamcrest.Matchers.contains;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.is;
import static org.junit.Assert.assertThat;

import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;
import org.mockito.Mockito;
import org.restlet.Request;
import org.restlet.data.Method;
import org.restlet.data.Reference;

import io.skysail.api.links.Link;
import io.skysail.api.links.LinkRelation;
import io.skysail.api.links.LinkRole;

public class LinkTest {

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
        Link link = new Link.Builder("uri").build();
        assertThat(link.getUri(), is(equalTo("uri")));
    }

    @Test
    public void builder_rejects_link_with_null_uri() throws Exception {
        thrown.expect(NullPointerException.class);
        String nullString = null;
        new Link.Builder(nullString).build();
    }
    
    @Test
    public void builder_method_rejects_link_with_null_uri() throws Exception {
        thrown.expect(NullPointerException.class);
        new Link.Builder("someurl").uri(null).build();
    }
    
    @Test
    public void builder_uri_method_sets_uri() throws Exception {
        Link link = new Link.Builder("someurl").uri("otherurl").build();
        assertThat(link.getUri(),is("otherurl"));
    }

    @Test
    public void builder_creates_link_with_default_linkrelation() throws Exception {
        Link link = new Link.Builder("uri").build();
        assertThat(link.getRel(), is(LinkRelation.ITEM));
    }

    @Test
    public void builder_creates_link_with_provided_title() throws Exception {
        Link link = new Link.Builder("uri").title("title").build();
        assertThat(link.getTitle(), is(equalTo("title")));
    }

    @Test
    public void builder_creates_link_which_needs_authentication_by_default() throws Exception {
        Link link = new Link.Builder("uri").title("title").build();
        assertThat(link.getNeedsAuthentication(), is(true));
    }

    @Test
    public void builder_creates_link_which_doesnot_need_authentication_if_switched_off() throws Exception {
        Link link = new Link.Builder("uri").title("title").authenticationNeeded(false).build();
        assertThat(link.getNeedsAuthentication(), is(false));
    }

    @Test
    public void creates_a_linkheader_segment_for_a_create_form_relation() {
        Link linkheader = new Link.Builder("uri").relation(LinkRelation.CREATE_FORM).build();
        assertThat(linkheader.toString("path"), is(equalTo("<pathuri>; rel=\"create-form\"; title=\"create-form\"; verbs=\"GET\"")));
    }

    @Test
    public void toString_gives_expected_value() {
        Link link = new Link.Builder("uri").build();
        assertThat(link.toString("path"),is (equalTo("<pathuri>; rel=\"item\"; title=\"item\"; verbs=\"GET\"")));
        link = new Link.Builder("uri").refId("refid").build();
        assertThat(link.toString("path"),is (equalTo("<pathuri>; rel=\"item\"; title=\"item\"; refId=\"refid\"; verbs=\"GET\"")));
        link = new Link.Builder("uri").title("theTitle").refId("refid").build();
        assertThat(link.toString("path"),is (equalTo("<pathuri>; rel=\"item\"; title=\"theTitle\"; refId=\"refid\"; verbs=\"GET\"")));
    }

    @Test
    public void parses_a_serialized_linkheader_to_an_linkheader_object() {
        assertThat(Link.valueOf(null), is(nullValue()));
        assertThat(Link.valueOf(""), is(nullValue()));
        String linkheader = "<pathuri>; rel=\"create-form\"; title=\"create-form\"; verbs=\"GET\"";
        assertThat(Link.valueOf(linkheader).getUri(), is(equalTo("pathuri")));
        assertThat(Link.valueOf(linkheader).getRel(), is(equalTo(LinkRelation.CREATE_FORM)));

    }

    @Test
    public void parses_a_serialized_linkheader_to_an_linkheader_object2() {
        String linkheader = "<pathuri>; rel=\"about\"; title=\"about me\"; refId=\"theId\"; verbs=\"POST\"";
        assertThat(Link.valueOf(linkheader).getUri(), is("pathuri"));
        assertThat(Link.valueOf(linkheader).getRel(), is(LinkRelation.ABOUT));
        assertThat(Link.valueOf(linkheader).getTitle(), is("about me"));
        assertThat(Link.valueOf(linkheader).getVerbs(), contains(Method.POST));
        assertThat(Link.valueOf(linkheader).getRefId(), is("theId"));
    }

    @Test
    public void parsing_throws_exception_for_broken_part() {
        thrown.expect(IllegalArgumentException.class);
        String linkheader = "<pathuri>; rel=; title=\"about me\"; verbs=\"POST\"";
        Link.valueOf(linkheader);
    }

    @Test
    public void createsBuilder_from_link() {
        Link linkTemplate = new Link.Builder("uri")
            .relation(LinkRelation.CREATE_FORM)
            .definingClass(String.class)
            .refId("refId")
            .build();
        Link linkFromTemplate = new Link.Builder(linkTemplate).build();
        assertThat(linkFromTemplate.getUri(),is("uri"));
        assertThat(linkFromTemplate.getRel(),is(LinkRelation.CREATE_FORM));
        assertThat(linkFromTemplate.getRefId(),is("refId"));
        assertThat(linkFromTemplate.getCls().getName(),is(String.class.getName()));
    }
    
    @Test
    public void button_is_not_shown_for_ListView_Role_Link() {
        Link link = new Link.Builder("uri").role(LinkRole.LIST_VIEW).build();
        assertThat(link.isShowAsButtonInHtml(), is(false));
    }

    @Test
    public void button_is_not_shown_for_menuItem_Role_Link() {
        Link link = new Link.Builder("uri").role(LinkRole.MENU_ITEM).build();
        assertThat(link.isShowAsButtonInHtml(), is(false));
    }

    @Test
    public void button_is_shown_for_GET_links_if_not_menuItem_or_listView() {
        Link link = new Link.Builder("uri").build();
        assertThat(link.isShowAsButtonInHtml(), is(true));
    }

}
