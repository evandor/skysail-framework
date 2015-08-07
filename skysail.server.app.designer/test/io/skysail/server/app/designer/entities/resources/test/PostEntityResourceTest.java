package io.skysail.server.app.designer.entities.resources.test;

import io.skysail.api.responses.SkysailResponse;
import io.skysail.server.app.designer.entities.Entity;

import org.junit.Test;

public class PostEntityResourceTest extends AbstractEntityResourceTest {

    @Test
    public void valid_form_data_yields_new_entity() {
        form.add("name", "TestApp");
        form.add("projectName", "TestProject");
        form.add("packageName", "io.skysail.testpackage");
        form.add("path", "../");
        SkysailResponse<Entity> result = postEntityResource.post(form, HTML_VARIANT);
        assertListResult(postEntityResource, result, "TestApp");
    }



}
