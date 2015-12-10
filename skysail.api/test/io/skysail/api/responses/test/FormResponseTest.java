package io.skysail.api.responses.test;

import static org.hamcrest.CoreMatchers.*;
import static org.junit.Assert.assertThat;

import org.junit.*;

import io.skysail.api.responses.FormResponse;

public class FormResponseTest {

    @Before
    public void setUp() throws Exception {
    }
    
    @Test
    public void constructor_with_entity_and_target_has_properly_set_attributes() {
        FormResponse<String> response = new FormResponse<String>(null, "entity", "target");
        assertThat(response.getEntity(), is(equalTo("entity")));
        assertThat(response.getTarget(), is(equalTo("target")));
        assertThat(response.getId(), is(nullValue()));
        assertThat(response.getRedirectBackTo(), is(nullValue()));
    }

    @Test
    public void constructor_with_entity_target_and_redirect_has_properly_set_attributes() {
        FormResponse<String> response = new FormResponse<String>(null, "entity", "target", "redirect");
        assertThat(response.getEntity(), is(equalTo("entity")));
        assertThat(response.getTarget(), is(equalTo("target")));
        assertThat(response.getId(), is(nullValue()));
               assertThat(response.getRedirectBackTo(), is(equalTo("redirect")));
    }

    @Test
    public void constructor_with_entity_target_id_and_redirect_has_properly_set_attributes() {
        FormResponse<String> response = new FormResponse<String>(null, "entity", "id", "target", "redirect");
        assertThat(response.getEntity(), is(equalTo("entity")));
        assertThat(response.getTarget(), is(equalTo("target")));
        assertThat(response.getId(), is(equalTo("id")));
        assertThat(response.getRedirectBackTo(), is(equalTo("redirect")));
    }


}
