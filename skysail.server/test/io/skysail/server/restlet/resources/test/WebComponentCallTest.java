package io.skysail.server.restlet.resources.test;

import static org.hamcrest.Matchers.*;
import static org.junit.Assert.assertThat;
import io.skysail.server.restlet.resources.*;
import io.skysail.server.restlet.resources.WebComponentCall.WebComponentCallBuilder;

import org.junit.*;

public class WebComponentCallTest {

    private WebComponentCallBuilder builder;

    @Before
    public void setUp() throws Exception {
        builder = WebComponentCall.builder()
        .type("sky-ajax-get")
        .id("theId")
        .title("title")
        .identifier("identifier")
        .requestUrl("requestUrl")
        .url("uri")
        .metadataUrl("metadataurl")
        .linkTo("linkTarget");
    }

    @Test
    public void creates_proper_html() throws Exception {
        WebComponentCall call = builder.build();
        assertThat(normalize(call.getHtml()), is(
                equalTo("<sky-ajax-get id=\"theId\" url=\"uri\" link-to=\"linkTarget\" metadata-url=\"metadataurl\" identifier=\"identifier\" request-url=\"requestUrl\" ></sky-ajax-get>")));
    }

    @Test
    public void creates_proper_html_with_glyph() throws Exception {
        WebComponentCall call = builder.glyphicon("glyph").build();
        assertThat(normalize(call.getHtml()), is(
                equalTo("<sky-ajax-get id=\"theId\" url=\"uri\" link-to=\"linkTarget\" metadata-url=\"metadataurl\" identifier=\"identifier\" glyphicon=\"glyph\" request-url=\"requestUrl\" ></sky-ajax-get>")));
    }


    @Test
    public void creates_proper_html_for_disabled_call() throws Exception {
        WebComponentCall call = builder.disabled(true).build();
        assertThat(normalize(call.getHtml()), is(
                equalTo("<sky-ajax-get id=\"theId\" url=\"uri\" link-to=\"linkTarget\" metadata-url=\"metadataurl\" identifier=\"identifier\" disabled request-url=\"requestUrl\" ></sky-ajax-get>")));
    }

    private String normalize(String html) {
        return html.replace("\n", "").replaceAll(" +", " ");
    }
}
